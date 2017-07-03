package orm.handler;

import beans.BeanManager;
import beans.EntityBeanI;
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
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class FindHandler implements InvocationHandler {

    private static final Logger logger = LogManager.getLogger(FindHandler.class);

    private final Map<String, Class<?>> genericParams;

    public FindHandler(Map<String, Class<?>> genericParams) {
        this.genericParams = genericParams;
    }

    private FindRet generateSQL(Class<?> type, String cmd) {
        String tableName = null;
        Entity entity = type.getDeclaredAnnotation(Entity.class);
        if (null == entity) {
            throw new RuntimeException("lack of Entity.class definition.");
        }
        tableName = entity.name();

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
                column2setter.put(column.name(), type.getMethod("set"+methodName, method.getReturnType()));
            } catch (NoSuchMethodException e) {
                logger.catching(e);
            }

            Id id = method.getDeclaredAnnotation(Id.class);
            if (null == id) {
                continue;
            }
            idColumn = column.name();
        }

        StringBuilder whereClause = new StringBuilder();
        int start = "findBy".length(),
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
        if (cmd.length() > "find".length()) {
            String method = cmd.substring(start);
            String column = method2column.get(method);
            if (null == column) {
                throw new RuntimeException("cmd column not exist, method : " + method);
            }
            whereClause.append('`').append(column).append("`=?");
        } else {
            if (null == idColumn) {
                throw new RuntimeException("id not exist.");
            }
            whereClause.append('`').append(idColumn).append("`=?");
        }

        return new FindRet()
                .setSql(String.format("SELECT %s FROM `%s` WHERE %s",
                        selectColumns.toString(), tableName, whereClause.toString()))
                .setColumn2setter(column2setter);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> type = null;
        Type genericReturnType = method.getGenericReturnType();
        if (this.genericParams.keySet().contains(genericReturnType.getTypeName())) {
            type = this.genericParams.get(genericReturnType.getTypeName());
        } else {
            type = method.getReturnType();
        }

        FindRet generatedSQL = this.generateSQL(type, method.getName());
        if (null == generatedSQL) {
            throw new RuntimeException("generate sql error.");
        }

        try (Connection conn = DatasourceFactory.getMySQLDataSource().getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(generatedSQL.getSql());

            int i = 1;
            for (Object arg : args) {
                SQLUtil.setPSbyFieldAtIndex(
                        preparedStatement, i++, arg, arg.getClass().getTypeName()
                );
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            logger.info(generatedSQL.getSql());
            if (!resultSet.next()) {
                return null;
            }

            Object ret = BeanManager.getInstance().createBean((Class<? extends EntityBeanI>) type);
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
        } catch (Throwable t) {
            logger.catching(t);
            return null;
        }
    }
}
