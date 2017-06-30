package rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.Configs;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

}
