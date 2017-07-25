package blog.controller;

import auth.AuthHelper;
import beans.BeanManager;
import blog.data.EIResourceLevel;
import blog.service.IResourceLevelService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;

public class SetResourceLevelCtrl extends WithMatcher {

    private IResourceLevelService resourceLevelService = BeanManager.getInstance().getService(IResourceLevelService.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "blog set resource level";
    }

    @Override
    public String urlPattern() {
        return "/blog/resource-level/set";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        if (!RestHelper.isPost(httpRequest, httpResponse)) {
            return;
        }

        EIResourceLevel resourceLevel = RestHelper.getBodyAsObject(httpRequest, EIResourceLevel.class);
        RestHelper.oneCallAndRet(httpResponse, this.resourceLevelService, "save", resourceLevel);
    }
}
