package blog.controller;

import auth.AuthHelper;
import beans.BeanManager;
import blog.data.EIResourceLevelMapping;
import blog.service.IResourceLevelMappingService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;

public class SetResourceLevelMappingCtrl extends WithMatcher {

    private IResourceLevelMappingService resourceLevelMappingService = BeanManager.getInstance().getService(IResourceLevelMappingService.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "blog set resource level mapping";
    }

    @Override
    public String urlPattern() {
        return "/blog/resource-level-mapping/list";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        if (!RestHelper.isPost(httpRequest, httpResponse)) {
            return;
        }

        EIResourceLevelMapping resourceLevelMapping = RestHelper.getBodyAsObject(httpRequest, EIResourceLevelMapping.class);
        RestHelper.oneCallAndRet(httpResponse, this.resourceLevelMappingService, "save", resourceLevelMapping);
    }
}
