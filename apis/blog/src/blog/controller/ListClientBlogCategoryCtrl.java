package blog.controller;

import beans.BeanManager;
import blog.service.IBlogCategoryService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;

public class ListClientBlogCategoryCtrl extends WithMatcher {

    private IBlogCategoryService blogCategoryService = BeanManager.getInstance().getService(IBlogCategoryService.class);

    @Override
    public String name() {
        return "blog list all client categories";
    }

    @Override
    public String urlPattern() {
        return "/blog/category/list/client";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        RestHelper.oneCallAndRet(httpResponse, this.blogCategoryService, "listNestedAllUnDisabled");
    }
}
