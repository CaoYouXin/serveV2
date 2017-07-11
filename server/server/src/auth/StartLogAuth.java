package auth;

import config.Configs;
import config.StartLog;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpContext;

import java.util.function.BiFunction;

public class StartLogAuth implements BiFunction<HttpRequest, HttpContext, Boolean> {
    @Override
    public Boolean apply(HttpRequest request, HttpContext context) {
        Header header = request.getLastHeader("infinitely-serve-token");
        if (null == header) {
            throw new AuthRuntimeException("请求头中未包含【infinitely-serve-token】.");
        }

        return header.getValue().equals(Configs.getConfigs(StartLog.CONFIG_KEY, String.class));
    }
}
