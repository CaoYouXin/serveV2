package orm.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import orm.DatasourceFactory;
import orm.handler.retO.RemoveRet;
import orm.util.SQLUtil;
import util.StringUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

public class RemoveHandler implements InvocationHandler {

    private static final Logger logger = LogManager.getLogger(RemoveHandler.class);

    private final Class<?> entityClass;

    public RemoveHandler(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RemoveRet generatedSQL = this.generateSQL(this.entityClass, method.getName());
        if (null == generatedSQL) {
            throw new RuntimeException("generate sql error.");
        }

        try (Connection conn = DatasourceFactory.getMySQLDataSource().getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(generatedSQL.getSql());
            logger.info(generatedSQL.getSql());

            if (null != args) {
                if (generatedSQL.isSoft()) {
                    Object at = args[args.length - 1];
                    System.arraycopy(args, 0, args, 1, args.length - 1);
                    args[0] = at;
                }

                int i = 1;
                for (Object arg : args) {
                    SQLUtil.setPSbyFieldAtIndex(
                            preparedStatement, i++, arg, arg.getClass().getTypeName()
                    );
                }
            }

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }

            return true;
        }
    }

    private RemoveRet generateSQL(Class<?> type, String cmd) {
        Entity entity = this.entityClass.getDeclaredAnnotation(Entity.class);
        if (null == entity) {
            throw new RuntimeException("entity declared lack of Entity.class.");
        }
        String tableName = entity.name();

        String idColumn = null;
        Map<String, String> method2column = new HashMap<>();
        for (Method method : type.getMethods()) {
            Column column = method.getDeclaredAnnotation(Column.class);
            if (null == column) {
                continue;
            }

            String methodName = StringUtil.cutPrefix(method.getName(), "get", "is");
            method2column.put(methodName, column.name());

            Id id = method.getDeclaredAnnotation(Id.class);
            if (null == id) {
                continue;
            }
            idColumn = column.name();
        }

        String columnName = null;
        StringBuilder whereClause = new StringBuilder();
        boolean isSoft = cmd.startsWith("softRemove");
        int start = isSoft ? "softRemoveBy".length() : "removeBy".length(),
                end = StringUtil.indexOf(cmd, start, "And", "Or", "At");
        while (-1 != end) {
            String method = cmd.substring(start, end);
            String column = method2column.get(method);
            if (null == column) {
                throw new RuntimeException("cmd column not exist, method : " + method);
            }

            boolean isAt = cmd.charAt(end + 1) == 't';
            boolean isAnd = cmd.charAt(end + 1) == 'n';
            whereClause.append('`')
                    .append(column)
                    .append("`=? ");
            if (!isAt) {
                whereClause.append(isAnd ? "AND " : "OR ");
            }
            start = end + (isAnd ? 3 : 2);
            end = StringUtil.indexOf(cmd, start, "And", "Or", "At");
        }
        if (cmd.equals("remove")) {
            if (null == idColumn) {
                throw new RuntimeException("id not exist.");
            }
            whereClause.append('`').append(idColumn).append("`=?");
        } else {
            String method = cmd.substring(start);
            String column = method2column.get(method);
            if (null == column) {
                throw new RuntimeException("cmd column not exist, method : " + method);
            }
            if (isSoft) {
                columnName = column;
            } else {
                whereClause.append('`').append(column).append("`=?");
            }
        }

        RemoveRet removeRet = new RemoveRet().setSoft(isSoft);
        if (isSoft) {
            return removeRet.setSql(String.format("UPDATE `%s` SET `%s`=? WHERE %s", tableName, columnName, whereClause.toString()));
        } else {
            return removeRet.setSql(String.format("DELETE FROM `%s` WHERE %s", tableName, whereClause.toString()));
        }
    }
}
