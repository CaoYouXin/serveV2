package orm.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import orm.DatasourceFactory;
import orm.handler.retO.SaveRet;
import orm.util.SQLUtil;
import util.StringUtil;

import javax.persistence.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class SaveHandler implements InvocationHandler {

    private static final Logger logger = LogManager.getLogger(SaveHandler.class);

    private final Class<?> tableClass;

    public SaveHandler(Class<?> tableClass) {
        this.tableClass = tableClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> type = args[0].getClass();
        if (!this.tableClass.isAssignableFrom(type)) {
            throw new RuntimeException("saving incorrect type : " + args[0].getClass());
        }

        SaveRet generatedSQL = this.generateSQL(this.tableClass, args[0]);
        if (null == generatedSQL) {
            throw new RuntimeException("generate sql error.");
        }

        try (Connection conn = DatasourceFactory.getMySQLDataSource().getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(generatedSQL.getSql(), Statement.RETURN_GENERATED_KEYS);
            logger.info(generatedSQL.getSql());

            int i = 1;
            for (Method getterMethod : generatedSQL.getGetters()) {
                SQLUtil.setPSbyFieldAtIndex(preparedStatement, i++,
                        getterMethod.invoke(args[0]),
                        getterMethod.getReturnType().getTypeName());
            }

            int affectedRows =preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }

            if (!generatedSQL.isInsert()) {
                return true;
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    SQLUtil.fill(generatedSQL.getIdSetter().getParameterTypes()[0].getTypeName(),
                            args[0], generatedSQL.getIdSetter(), generatedKeys, true, 1, null);
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    private SaveRet generateSQL(Class<?> type, Object arg) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Entity entity = type.getDeclaredAnnotation(Entity.class);
        if (null == entity) {
            throw new RuntimeException("lack of Entity.class definition.");
        }
        String tableName = entity.name();

        Boolean isInsert = null;
        Method idSetter = null;
        for (Method method : type.getMethods()) {
            Id id = method.getDeclaredAnnotation(Id.class);
            if (null == id) {
                continue;
            }

            Column column = method.getDeclaredAnnotation(Column.class);
            Object idValue = method.invoke(arg);

            isInsert = null == idValue;
            String setterName = "set" + StringUtil.cutPrefix(method.getName(), "get", "is");
            idSetter = type.getMethod(setterName, method.getReturnType());
            break;
        }

        SaveRet generatedSQL = null;
        if (isInsert) {
            generatedSQL = this.generateInsert(type, tableName);
        } else {
            generatedSQL = this.generateUpdate(type, tableName);
        }

        return new SaveRet().setInsert(isInsert).setIdSetter(idSetter)
                .setSql(generatedSQL.getSql())
                .setGetters(generatedSQL.getGetters());
    }

    private SaveRet generateUpdate(Class<?> type, String tableName) {
        String idColumn = null;
        StringJoiner columnSetters = new StringJoiner(",");

        Method idGetter = null;
        List<Method> getters = new ArrayList<>();
        for (Method method : type.getMethods()) {
            Column column = method.getDeclaredAnnotation(Column.class);
            if (null == column) {
                continue;
            }

            Id id = method.getDeclaredAnnotation(Id.class);
            if (null != id) {
                idColumn = column.name();
                idGetter = method;
                continue;
            }

            columnSetters.add(String.format("`%s`=?", column.name()));
            getters.add(method);
        }
        getters.add(idGetter);

        return new SaveRet().setSql(String.format(
                "UPDATE `%s` SET %s WHERE `%s`=?", tableName,
                columnSetters.toString(), idColumn
        )).setGetters(getters);
    }

    private SaveRet generateInsert(Class<?> type, String tableName) {
        StringJoiner columns = new StringJoiner("`,`", "`", "`");
        StringJoiner valuePlaceHolders = new StringJoiner(",");

        List<Method> getters = new ArrayList<>();
        for (Method method : type.getMethods()) {
            Column column = method.getDeclaredAnnotation(Column.class);
            if (null == column) {
                continue;
            }

            Id id = method.getDeclaredAnnotation(Id.class);
            if (null == id) {
                columns.add(column.name());
                valuePlaceHolders.add("?");
                getters.add(method);
                continue;
            }

            GeneratedValue generatedValue = method.getDeclaredAnnotation(GeneratedValue.class);
            if (null != generatedValue) {
                if (!generatedValue.strategy().equals(GenerationType.IDENTITY)) {
                    throw new RuntimeException("don't know how to insert, id is null");
                }
            }
        }

        return new SaveRet().setSql(String.format(
                "INSERT INTO `%s`(%s) VALUES (%s)", tableName,
                columns.toString(), valuePlaceHolders.toString()
        )).setGetters(getters);
    }
}
