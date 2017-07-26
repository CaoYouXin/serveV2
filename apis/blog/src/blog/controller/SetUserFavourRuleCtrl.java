package blog.controller;

import auth.AuthHelper;
import beans.BeanManager;
import blog.data.EIUserFavourRule;
import blog.service.IUserFavourRuleService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;

public class SetUserFavourRuleCtrl extends WithMatcher {

    private IUserFavourRuleService userFavourRuleService = BeanManager.getInstance().getService(IUserFavourRuleService.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "blog set user favour rule";
    }

    @Override
    public String urlPattern() {
        return "/blog/user-favour-rule/set";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        if (!RestHelper.isPost(httpRequest, httpResponse)) {
            return;
        }

        EIUserFavourRule userFavourRule = RestHelper.getBodyAsObject(httpRequest, EIUserFavourRule.class);
        RestHelper.oneCallAndRet(httpResponse, this.userFavourRuleService, "save", userFavourRule);
    }
}
