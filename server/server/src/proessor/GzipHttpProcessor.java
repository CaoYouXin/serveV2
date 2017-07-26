package proessor;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.GzipCompressingEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class GzipHttpProcessor implements HttpProcessor {

    private static final Logger logger = LogManager.getLogger(GzipHttpProcessor.class);

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        Header acceptEncoding = request.getLastHeader("Accept-Encoding");
        if ("*".equals(acceptEncoding.getValue()) || acceptEncoding.getValue().contains("gzip")) {

            context.setAttribute("infinitely-serve-gzip", true);
        }
    }

    @Override
    public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
        if ((Boolean) context.getAttribute("infinitely-serve-gzip")) {

            logger.info("--- using gzip");

            response.setEntity(new GzipCompressingEntity(response.getEntity()));
        }
    }
}
