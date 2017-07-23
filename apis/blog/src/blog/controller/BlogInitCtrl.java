package blog.controller;

import auth.AuthHelper;
import blog.auth.AuthHelperExt;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.HelperController;
import rest.JsonResponse;
import rest.RestHelper;

import java.io.IOException;

public class BlogInitCtrl extends HelperController {
    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "blog init";
    }

    @Override
    public String urlPattern() {
        return "/blog/init";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        AuthHelperExt.addExtAuth();

        RestHelper.responseJSON(httpResponse, JsonResponse.success("操作成功！"));
    }
}
