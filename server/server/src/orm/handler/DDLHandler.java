package orm.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import orm.DatasourceFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.StringJoiner;

public class DDLHandler implements InvocationHandler {

    private static final Logger logger = LogManager.getLogger(DDLHandler.class);

    private final Class<?> entityClass;
    private final Class<?> idClass;

    public DDLHandler(String[] genericNames, Map<String, Class<?>> genericParams) {
        this.entityClass = genericParams.get(genericNames[0]);
        this.idClass = genericParams.get(genericNames[1]);

        this.verifyEntityId();
    }

    private void verifyEntityId() {
        for (Method method : this.entityClass.getMethods()) {
            Id declaredAnnotation = method.getDeclaredAnnotation(Id.class);
            if (null == declaredAnnotation) {
                continue;
            }

            if (!method.getReturnType().equals(this.idClass)) {
                throw new RuntimeException(String.format(
                        "entity class's id column not identical. %s vs %s",
                        method.getReturnType(), this.idClass
                ));
            }
        }
    }

    private String generateDDL(String tableName) {
        StringBuilder columnDefs = new StringBuilder();
        StringJoiner keyColumns = new StringJoiner("`,`", "(`", "`)");
        StringBuilder keyColumnDef = new StringBuilder();
        StringBuilder uniqueColumnsDef = new StringBuilder();

        boolean idColumnExist = false;
        for (Method method : this.entityClass.getMethods()) {
            Column column = method.getDeclaredAnnotation(Column.class);
            if (null == column) {
                continue;
            }

            columnDefs.append('`')
                    .append(column.name())
                    .append('`')
                    .append(' ')
                    .append(this.getTypeDef(method.getReturnType().getTypeName(), column));

            if (column.unique()) {
                uniqueColumnsDef.append(String.format(", UNIQUE INDEX `%s_UNIQUE` (`%s` ASC)", column.name(), column.name()));
            }

            Id id = method.getDeclaredAnnotation(Id.class);
            if (null == id) {
                columnDefs.append(column.nullable() ? "" : " NOT")
                        .append(" NULL")
                        .append(',');
                continue;
            }
            idColumnExist = true;
            keyColumns.add(column.name());
            columnDefs.append(' ').append("NOT NULL");

            GeneratedValue generatedValue = method.getDeclaredAnnotation(GeneratedValue.class);
            if (null == generatedValue) {
                throw new RuntimeException("id lack of generated Value strategy.");
            }

            switch (generatedValue.strategy()) {
                case IDENTITY:
                    if (this.canAutoIncrement(method.getReturnType())) {
                        columnDefs.append(" AUTO_INCREMENT");
                    }
                    columnDefs.append(',');
                    break;
                default:
                    throw new RuntimeException("unhandled generated value strategy.");
            }
        }

        if (idColumnExist) {
            keyColumnDef.append("PRIMARY KEY ").append(keyColumns.toString());
        } else {
            throw new RuntimeException("no primary key error.");
        }

        return String.format("CREATE TABLE `%s` (%s%s%s);"
                , tableName, columnDefs.toString(), keyColumnDef.toString(), uniqueColumnsDef.toString());
    }

    private boolean canAutoIncrement(Class<?> type) {
        return type.equals(Long.class)
                || type.equals(Integer.class)
                || type.equals(Short.class)
                || type.equals(Byte.class);
    }

    private String getTypeDef(String typeName, Column column) {
        switch (typeName) {
            case "java.lang.Boolean":
            case "java.lang.Byte":
                return "TINYINT";
            case "java.lang.Short":
                return "SMALLINT";
            case "java.lang.Integer":
                return "INT";
            case "java.lang.Long":
                return "BIGINT";
            case "java.lang.String":
                if (column.length() <= 1 << 8 - 1) {
                    return "VARCHAR(" + column.length() + ")";
                }

                if (column.length() <= 1 << 16 - 1) {
                    return "TEXT";
                }

                if (column.length() <= 1 << 24 - 1) {
                    return "MEDIUMTEXT";
                }

                if (column.length() <= Integer.MAX_VALUE * 2L + 1) {
                    return "LONGTEXT";
                }

                throw new RuntimeException("string too long.");
            case "java.util.Date":
                return "DATETIME";
            default:
                throw new RuntimeException("not known type " + typeName);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Entity entity = this.entityClass.getDeclaredAnnotation(Entity.class);
        if (null == entity) {
            throw new RuntimeException("entity declared lack of Entity.class.");
        }
        String tableName = entity.name();

        try (Connection conn = DatasourceFactory.getMySQLDataSource().getConnection()) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("show tables;");
            while (resultSet.next()) {
                if (tableName.equals(resultSet.getString(1))) {
                    return true;
                }
            }

            String generatedDDL = this.generateDDL(tableName);
            logger.info(generatedDDL);
            statement.execute(generatedDDL);
            return true;
        } catch (Throwable t) {
            logger.catching(t);
            return false;
        }
    }
}
