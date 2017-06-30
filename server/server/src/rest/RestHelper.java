package rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.Configs;
import org.apache.http.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;

public class RestHelper {

    private static final Logger logger = LogManager.getLogger(RestHelper.class);

    public static void responseJSON(HttpResponse response, Object json) {
        ObjectMapper objectMapper = Configs.getConfigs(Configs.OBJECT_MAPPER, ObjectMapper.class);

        String retString = null;
        try {
            retString = objectMapper.writeValueAsString(json);
        } catch (JsonProcessingException e) {
            logger.catching(e);
            response.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        response.setStatusCode(HttpStatus.SC_OK);
        response.setEntity(new StringEntity(
                retString,
                ContentType.APPLICATION_JSON
        ));
    }

    public static void responseHTML(HttpResponse response, int code, String html) {
        response.setStatusCode(code);
        response.setEntity(new StringEntity(
                html,
                ContentType.create("text/html", "UTF-8")
        ));
    }

    public static HttpEntity getBody(HttpRequest httpRequest) {
        if (httpRequest instanceof HttpEntityEnclosingRequest) {
            return ((HttpEntityEnclosingRequest) httpRequest).getEntity();
        }
        return null;
    }

    public static String getMethod(HttpRequest httpRequest) {
        return httpRequest.getRequestLine().getMethod().toUpperCase(Locale.ROOT);
    }

    public static boolean isGet(HttpRequest httpRequest) {
        return getMethod(httpRequest).equals("GET");
    }

    public static boolean isPost(HttpRequest httpRequest) {
        return getMethod(httpRequest).equals("POST");
    }

    public static boolean isOptions(HttpRequest httpRequest) {
        return getMethod(httpRequest).equals("OPTIONS");
    }

    public static String restUri(HttpRequest request, String startWith) {
        String uri = request.getRequestLine().getUri();
        if (uri.startsWith(startWith)) {
            return uri.substring(startWith.length());
        }
        return null;
    }

}
