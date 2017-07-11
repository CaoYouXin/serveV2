package hanlder;

import auth.ServeAuth;
import config.Configs;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import rest.JsonResponse;
import rest.RestHelper;

import java.io.IOException;

public class ServeHandler implements HttpRequestHandler {

    private final HttpRequestHandler handler;

    public ServeHandler(String docRoot, String urlRoot) {
        this.handler = new HttpFileHandler(docRoot, urlRoot);
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        ServeAuth serveAuth = Configs.getConfigs(Configs.SERVE_AUTH, ServeAuth.class);

        try {
            if (!serveAuth.apply(request, context)) {
                RestHelper.responseJSON(response, JsonResponse.fail(40002, "未授权的资源访问."));
                return;
            }
        } catch (Throwable e) {
            RestHelper.responseJSON(response, JsonResponse.fail(40002, e.getMessage()));
            return;
        }

        this.handler.handle(request, response, context);
    }
}
