package auth;

import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpContext;

import java.util.function.BiFunction;

public class AlwaysTrueAuth implements BiFunction<HttpRequest, HttpContext, Boolean> {

    @Override
    public Boolean apply(HttpRequest request, HttpContext context) {
        return true;
    }
}
