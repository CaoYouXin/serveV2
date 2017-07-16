package blog.controller;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.HelperController;

import java.io.IOException;

public class UserRegisterCtrl extends HelperController {
    @Override
    public String name() {
        return "blog user register";
    }

    @Override
    public String urlPattern() {
        return "/blog/user/register";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {

    }
}
