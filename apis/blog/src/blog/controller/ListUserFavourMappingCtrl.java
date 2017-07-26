package blog.controller;

import auth.AuthHelper;
import beans.BeanManager;
import blog.service.IUserFavourMappingService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;

public class ListUserFavourMappingCtrl extends WithMatcher {

    private IUserFavourMappingService userFavourMappingService = BeanManager.getInstance().getService(IUserFavourMappingService.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "blog list user favour mapping";
    }

    @Override
    public String urlPattern() {
        return "/blog/user-favour-mapping/list";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        RestHelper.oneCallAndRet(httpResponse, this.userFavourMappingService, "list");
    }
}
