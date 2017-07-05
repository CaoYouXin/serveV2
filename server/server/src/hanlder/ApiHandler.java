package hanlder;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rest.Controller;
import rest.RestHelper;

import java.io.IOException;
import java.util.List;

public class ApiHandler implements HttpRequestHandler {

    private static final Logger logger = LogManager.getLogger(ApiHandler.class);

    private List<Controller> controllers;

    public ApiHandler(List<Controller> controllers) {
        this.controllers = controllers;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        for (Controller controller : this.controllers) {
            if (controller.getUriPatternMatcher().match(request)) {

                logger.info(request.getRequestLine());

                RestHelper.crossOrigin(request, response);
                if (RestHelper.isOptions(request)) {
                    return;
                }

                controller.handle(request, response, context);
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
