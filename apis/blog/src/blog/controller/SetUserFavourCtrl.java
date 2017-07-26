package blog.controller;

import auth.AuthHelper;
import beans.BeanManager;
import blog.data.EIUserFavour;
import blog.service.IUserFavourService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;

public class SetUserFavourCtrl extends WithMatcher {

    private IUserFavourService userFavourService = BeanManager.getInstance().getService(IUserFavourService.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "blog set user favour";
    }

    @Override
    public String urlPattern() {
        return "/blog/user-favour/set";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        if (!RestHelper.isPost(httpRequest, httpResponse)) {
            return;
        }

        EIUserFavour userFavour = RestHelper.getBodyAsObject(httpRequest, EIUserFavour.class);
        RestHelper.oneCallAndRet(httpResponse, this.userFavourService, "save", userFavour);
    }
}
