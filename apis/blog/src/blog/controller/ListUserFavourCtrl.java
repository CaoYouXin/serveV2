package blog.controller;

import auth.AuthHelper;
import beans.BeanManager;
import blog.service.IUserFavourService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;

public class ListUserFavourCtrl extends WithMatcher {

    private IUserFavourService userFavourService = BeanManager.getInstance().getService(IUserFavourService.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "blog list user favour";
    }

    @Override
    public String urlPattern() {
        return "/blog/user-favour/list";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        RestHelper.oneCallAndRet(httpResponse, this.userFavourService, "list");
    }
}
