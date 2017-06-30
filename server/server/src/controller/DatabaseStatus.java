package controller;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.Controller;
import rest.HelperController;
import rest.JsonResponse;
import rest.RestHelper;

import java.io.IOException;
import java.util.Map;

public class DatabaseStatus extends HelperController implements Controller {
    @Override
    public String name() {
        return "database status";
    }

    @Override
    public String urlPattern() {
        return "/database/:date/status";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        Map<String, String> params = this.getUriPatternMatcher().getParams(request);

        RestHelper.responseJSON(response, JsonResponse.fail(20000,
                String.format("param date : %s", params.get("date"))));
    }
}
