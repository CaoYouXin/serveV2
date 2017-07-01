package hanlder;

import org.apache.http.*;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rest.Controller;
import rest.RestHelper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ApiHandler implements HttpRequestHandler {

    private static final Logger logger = LogManager.getLogger(ApiHandler.class);

    private List<Controller> controllers;

    public ApiHandler(List<Controller> controllers) {
        this.controllers = controllers;
    }

    private void setCORS(HttpRequest httpRequest, String reqHeaderName, HttpResponse httpResponse, String resHeaderName) {
        Header[] headers = httpRequest.getHeaders(reqHeaderName);
        if (headers.length > 0) {
            logger.info(reqHeaderName + " : " + Arrays.toString(headers));
        }

        if (headers.length > 0) {
            for (Header header : headers) {
                httpResponse.setHeader(resHeaderName, header.getValue());
            }
        }
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        for (Controller controller : this.controllers) {
            if (controller.getUriPatternMatcher().match(request)) {

                logger.info(request.getRequestLine());

                setCORS(request, "Origin",
                        response, "Access-Control-Allow-Origin");

                setCORS(request, "Access-Control-Allow-Method",
                        response, "Access-Control-Request-Methods");

                setCORS(request, "Access-Control-Request-Headers",
                        response, "Access-Control-Allow-Headers");

                setCORS(request, "Access-Control-Allow-Credentials",
                        response, "Access-Control-Allow-Credentials");

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
