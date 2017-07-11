package auth;

import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpContext;

public class FalseServeAuth implements ServeAuth {
    @Override
    public Boolean apply(HttpRequest request, HttpContext context) {
        return false;
    }
}
