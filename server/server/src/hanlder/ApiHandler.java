package hanlder;

import auth.AuthHelper;
import auth.AuthRuntimeException;
import config.Configs;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rest.*;

import java.io.IOException;
import java.util.List;

public class ApiHandler implements HttpRequestHandler {

    private static final Logger logger = LogManager.getLogger(ApiHandler.class);

    private List<Interceptor> interceptors;
    private List<Controller> controllers;

    public ApiHandler(List<Controller> controllers) {
        this.controllers = controllers;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        for (Controller controller : this.controllers) {
            if (controller.getUriPatternMatcher().match(request, null)) {

                logger.info(request.getRequestLine());

                RestHelper.crossOrigin(request, response);
                if (RestHelper.isOptions(request)) {
                    return;
                }

                try {
                    if (!AuthHelper.auth(controller.auth(), request, context)) {
                        RestHelper.responseJSON(response, JsonResponse.fail(RestCode.UNAUTHED, "未授权的访问."));
                        return;
                    }
                } catch (AuthRuntimeException e) {
                    RestHelper.responseJSON(response, JsonResponse.fail(RestCode.UNAUTHED, e.getMessage()));
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

                controller.handle(request, response, context);

                List<Interceptor> matchedPost = RestHelper.matchedPostInterceptors(this.interceptors, request);
                for (Interceptor interceptor : matchedPost) {
                    interceptor.handle(request, response, context);
                }

                return;
            }
        }

        String uri = request.getRequestLine().getUri();

        RestHelper.responseHTML(response, HttpStatus.SC_NOT_FOUND,
                "<html><body><p style=\"text-align: center;\">Request Api "
                        + "<br/><strong>" + uri + "</strong><br/>"
                        + " not found</p></body></html>");
    }
}
