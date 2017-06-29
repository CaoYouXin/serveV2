package hanlder;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

import java.io.IOException;

public class FallbackHandler implements HttpRequestHandler {
    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        String uri = request.getRequestLine().getUri();

        StringEntity entity = new StringEntity(
                "<html><body><p style=\"text-align: center;\">Request Handler "
                        + "<br/><strong>" + uri + "</strong><br/>"
                        + " not found</p></body></html>",
                ContentType.create("text/html", "UTF-8"));
        response.setEntity(entity);
        response.setStatusCode(HttpStatus.SC_NOT_FOUND);
    }
}
