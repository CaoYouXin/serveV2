package hanlder;

import auth.ServeAuth;
import config.Configs;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rest.Interceptor;
import rest.JsonResponse;
import rest.RestCode;
import rest.RestHelper;

import java.io.IOException;
import java.util.List;

public class ServeHandler implements HttpRequestHandler {

    private static final Logger logger = LogManager.getLogger(ServeHandler.class);

    private final HttpRequestHandler handler;

    private List<Interceptor> interceptors;

    public ServeHandler(String docRoot, String urlRoot) {
        this.handler = new HttpFileHandler(docRoot, urlRoot, "");
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        ServeAuth serveAuth = Configs.getConfigs(Configs.SERVE_AUTH, ServeAuth.class);

        logger.info(request.getRequestLine());

        try {
            if (!serveAuth.apply(request, context)) {
                RestHelper.responseJSON(response, JsonResponse.fail(RestCode.FORBIDDEN_RESOURCE, "未授权的资源访问."));
                return;
            }
        } catch (Throwable e) {
            RestHelper.responseJSON(response, JsonResponse.fail(RestCode.FORBIDDEN_RESOURCE, e.getMessage()));
            return;
        }

        if (null == this.interceptors || !Configs.getConfigs(Interceptor.INTERCEPTOR_CONFIG_KEY, Boolean.class)) {
            this.interceptors = RestHelper.loadInterceptors();
            Configs.setConfigs(Interceptor.INTERCEPTOR_CONFIG_KEY, true);
        }

        List<Interceptor> matchedPre = RestHelper.matchedPreInterceptors(this.interceptors, request);
        for (Interceptor interceptor : matchedPre) {
            interceptor.handle(request, response, context);
        }

        this.handler.handle(request, response, context);

        List<Interceptor> matchedPost = RestHelper.matchedPostInterceptors(this.interceptors, request);
        for (Interceptor interceptor : matchedPost) {
            interceptor.handle(request, response, context);
        }
    }
}
