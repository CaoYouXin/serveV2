package orm.handler;

import beans.BeanManager;
import beans.EntityBeanI;
import config.Configs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import orm.DatasourceFactory;
import orm.handler.retO.FindRet;
import orm.util.SQLUtil;
import util.StringUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class FindHandler implements InvocationHandler {

    private static final Logger logger = LogManager.getLogger(FindHandler.class);

    private final Map<String, Class<?>> genericParams;

    public FindHandler(Map<String, Class<?>> genericParams) {
        this.genericParams = genericParams;
    }

    private FindRet generateSQL(Class<?> type, String cmd) {
        Entity entity = type.getDeclaredAnnotation(Entity.class);
        if (null == entity) {
            throw new RuntimeException("lack of Entity.class definition.");
        }
        String tableName = entity.name();

        Map<String, String> method2column = new HashMap<>();
        Map<String, Method> column2setter = new HashMap<>();
        String idColumn = null;

        StringJoiner selectColumns = new StringJoiner("`,`", "`", "`");
        for (Method method : type.getMethods()) {
            Column column = method.getDeclaredAnnotation(Column.class);
            if (null == column) {
                continue;
            }

            selectColumns.add(column.name());
            String methodName = StringUtil.cutPrefix(method.getName(), "get", "is");
            method2column.put(methodName, column.name());
            try {
                column2setter.put(column.name(), type.getMethod("set" + methodName, method.getReturnType()));
            } catch (NoSuchMethodException e) {
                logger.catching(e);
            }

            Id id = method.getDeclaredAnnotation(Id.class);
            if (null == id) {
                continue;
            }
            idColumn = column.name();
        }

        boolean retList = cmd.length() > "find".length() && cmd.charAt("find".length()) == 'A';
        StringBuilder whereClause = new StringBuilder();
        int start = retList ? "findAllBy".length() : "findBy".length(),
                end = StringUtil.indexOf(cmd, start, "And", "Or");
        while (-1 != end) {
            String method = cmd.substring(start, end);
            String column = method2column.get(method);
            if (null == column) {
                throw new RuntimeException("cmd column not exist, method : " + method);
            }

            boolean isAnd = cmd.charAt(end + 1) == 'A';
            whereClause.append('`')
                    .append(column)
                    .append("`=? ")
                    .append(isAnd ? "AND " : "OR ");
            start = end + (isAnd ? 3 : 2);
            end = StringUtil.indexOf(cmd, start, "And", "Or");
        }
        if (retList ? cmd.length() > "findAll".length() : cmd.length() > "find".length()) {
            String method = cmd.substring(start);
            String column = method2column.get(method);
            if (null == column) {
                throw new RuntimeException("cmd column not exist, method : " + method);
            }
            whereClause.append('`').append(column).append("`=?");
        } else if (cmd.equals("find")) {
            if (null == idColumn) {
                throw new RuntimeException("id not exist.");
            }
            whereClause.append('`').append(idColumn).append("`=?");
        }
        if (whereClause.length() > 0) {
            whereClause.insert(0, "WHERE ");
        }

        return new FindRet()
                .setSql(String.format("SELECT %s FROM `%s` %s",
                        selectColumns.toString(), tableName, whereClause.toString()))
                .setColumn2setter(column2setter).setListAll(retList);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> type = null;
        Type genericReturnType = method.getGenericReturnType();
        if (this.genericParams.keySet().contains(genericReturnType.getTypeName())) {
            type = this.genericParams.get(genericReturnType.getTypeName());
        } else if (genericReturnType.getTypeName().startsWith("java.util.List")) {
            String typeName = genericReturnType.getTypeName();
            typeName = typeName.substring(typeName.indexOf('<') + 1, typeName.indexOf('>'));
            if (this.genericParams.keySet().contains(typeName)) {
                type = this.genericParams.get(typeName);
            } else {
                ClassLoader classLoader = Configs.getConfigs(Configs.CLASSLOADER, ClassLoader.class,
                        () -> getClass().getClassLoader());
                type = classLoader.loadClass(typeName);
            }
        } else {
            type = method.getReturnType();
        }

        FindRet generatedSQL = this.generateSQL(type, method.getName());
        if (null == generatedSQL) {
            throw new RuntimeException("generate sql error.");
        }

        try (Connection conn = DatasourceFactory.getMySQLDataSource().getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(generatedSQL.getSql());

            if (null != args) {
                int i = 1;
                for (Object arg : args) {
                    SQLUtil.setPSbyFieldAtIndex(
                            preparedStatement, i++, arg, arg.getClass().getTypeName()
                    );
                }
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            logger.info(generatedSQL.getSql());
            if (generatedSQL.getListAll()) {
                List ret = new ArrayList();
                while (resultSet.next()) {
                    ret.add(getObject((Class<? extends EntityBeanI>) type, generatedSQL, resultSet));
                }
                return ret;
            } else {
                if (!resultSet.next()) {
                    return null;
                }

                Object ret = getObject((Class<? extends EntityBeanI>) type, generatedSQL, resultSet);
                return ret;
            }
        } catch (Throwable t) {
            logger.catching(t);
            return null;
        }
    }

    private Object getObject(Class<? extends EntityBeanI> type, FindRet generatedSQL, ResultSet resultSet) throws SQLException, InvocationTargetException, IllegalAccessException {
        Object ret = BeanManager.getInstance().createBean(type);
        Map<String, Method> column2setter = generatedSQL.getColumn2setter();
        for (String column : column2setter.keySet()) {
            Method setterMethod = column2setter.get(column);
            SQLUtil.fill(
                    setterMethod.getParameterTypes()[0].getTypeName(),
                    ret, setterMethod, resultSet,
                    false, null, column
            );
        }
        return ret;
    }
}
