package rest;

public interface Interceptor extends Controller {

    String INTERCEPTOR_CONFIG_KEY = "INTERCEPTOR_CONFIG_KEY";

    default boolean isPost() {
        return true;
    }

}
