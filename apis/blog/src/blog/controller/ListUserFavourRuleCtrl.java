package blog.controller;

import auth.AuthHelper;
import beans.BeanManager;
import blog.service.IUserFavourRuleService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;

public class ListUserFavourRuleCtrl extends WithMatcher {

    private IUserFavourRuleService userFavourRuleService = BeanManager.getInstance().getService(IUserFavourRuleService.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "blog list user favour rule";
    }

    @Override
    public String urlPattern() {
        return "/blog/user-favour-rule/list";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        RestHelper.oneCallAndRet(httpResponse, this.userFavourRuleService, "list");
    }
}
