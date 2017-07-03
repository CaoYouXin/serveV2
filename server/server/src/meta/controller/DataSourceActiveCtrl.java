package meta.controller;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.Controller;
import rest.HelperController;

import java.io.IOException;

public class DataSourceActiveCtrl extends HelperController implements Controller {
    @Override
    public String name() {
        return "meta DataSource Active";
    }

    @Override
    public String urlPattern() {
        return "/database/init/:schema";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {

    }
}
