package beans;

public class BeanNotInitException extends RuntimeException {
    public BeanNotInitException(String message) {
        super(message);
    }
}
