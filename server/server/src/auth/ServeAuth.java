package auth;

import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpContext;

import java.util.function.BiFunction;

public interface ServeAuth extends BiFunction<HttpRequest, HttpContext, Boolean> {
}
