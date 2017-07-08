package hanlder;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import rest.RestHelper;

import java.io.IOException;

public class FallbackHandler implements HttpRequestHandler {
    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        String uri = request.getRequestLine().getUri();

        RestHelper.responseHTML(response, HttpStatus.SC_NOT_FOUND,
                "<html><body><p style=\"text-align: center;\">Request Handler "
                        + "<br/><strong>" + uri + "</strong><br/>"
                        + " not found</p></body></html>");
    }
}
