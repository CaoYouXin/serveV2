package blog.service.exp;

public class TableNotCreateException extends RuntimeException {
    public TableNotCreateException(String tableName) {
        super(String.format("[%s] table can not be created.", tableName));
    }
}
