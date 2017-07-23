package blog.service.exp;

public class TableNotSaveException extends RuntimeException {
    public TableNotSaveException(String name) {
        super(String.format("can not save [%s]", name));
    }
}
