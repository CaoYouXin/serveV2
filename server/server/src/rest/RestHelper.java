package rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.Configs;
import org.apache.http.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Locale;

public class RestHelper {

    private static final Logger logger = LogManager.getLogger(RestHelper.class);

    public static void crossOrigin(HttpRequest request, HttpResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");

//        RestHelper.setCORS(request, "Origin",
//                response, "Access-Control-Allow-Origin");

        RestHelper.setCORS(request, "Access-Control-Allow-Method",
                response, "Access-Control-Request-Methods");

        RestHelper.setCORS(request, "Access-Control-Request-Headers",
                response, "Access-Control-Allow-Headers");

        RestHelper.setCORS(request, "Access-Control-Allow-Credentials",
                response, "Access-Control-Allow-Credentials");
    }

    public static void setCORS(HttpRequest httpRequest, String reqHeaderName, HttpResponse httpResponse, String resHeaderName) {
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

    public static void responseJSON(HttpResponse response, Object json) {
        ObjectMapper objectMapper = Configs.getConfigs(Configs.OBJECT_MAPPER, ObjectMapper.class, () -> new ObjectMapper());

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
        logger.info(retString);
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

    public static String getBodyAsString(HttpRequest httpRequest) {
        HttpEntity entity = getBody(httpRequest);
        if (null != entity) {
            try {
                return EntityUtils.toString(entity, Consts.UTF_8);
            } catch (IOException e) {
                logger.catching(e);
            }
        }
        return "";
    }

    public static byte[] getBodyAsBytes(HttpRequest httpRequest) {
        HttpEntity entity = getBody(httpRequest);
        if (null != entity) {
            try {
                return EntityUtils.toByteArray(entity);
            } catch (IOException e) {
                logger.catching(e);
            }
        }
        return new byte[0];
    }

    public static <T> T getBodyAsObject(HttpRequest httpRequest, Class<T> clazz) {
        String bodyAsString = getBodyAsString(httpRequest);
        ObjectMapper objectMapper = Configs.getConfigs(Configs.OBJECT_MAPPER, ObjectMapper.class, () -> new ObjectMapper());
        try {
            return objectMapper.readValue(bodyAsString, clazz);
        } catch (IOException e) {
            logger.catching(e);
            return null;
        }
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

    public static boolean isInvalidMethod(HttpRequest httpRequest) {
        String method = getMethod(httpRequest);
        return !method.equals("GET") && !method.equals("HEAD") && !method.equals("POST");
    }

    public static String getDecodedUrl(HttpRequest httpRequest) {
        try {
            return URLDecoder.decode(httpRequest.getRequestLine().getUri(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.catching(e);
            return "";
        }
    }

}
