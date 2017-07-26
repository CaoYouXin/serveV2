package blog.controller;

import auth.AuthHelper;
import beans.BeanManager;
import blog.data.EIUserFavourMapping;
import blog.service.IUserFavourMappingService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;

public class SetUserFavourMappingCtrl extends WithMatcher {

    private IUserFavourMappingService userFavourMappingService = BeanManager.getInstance().getService(IUserFavourMappingService.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "blog set user favour mapping";
    }

    @Override
    public String urlPattern() {
        return "/blog/user-favour-mapping/set";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        if (!RestHelper.isPost(httpRequest, httpResponse)) {
            return;
        }

        EIUserFavourMapping userFavourMapping = RestHelper.getBodyAsObject(httpRequest, EIUserFavourMapping.class);
        RestHelper.oneCallAndRet(httpResponse, this.userFavourMappingService, "save", userFavourMapping);
    }
}
