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

public class DatabaseStatus extends HelperController implements Controller {
    @Override
    public String name() {
        return "database status";
    }

    @Override
    public String urlPattern() {
        return "/database/status";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        RestHelper.responseJSON(response, JsonResponse.fail(40400, "还未实现！"));
    }
}
