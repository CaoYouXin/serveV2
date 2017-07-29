package blog.controller;

import auth.AuthHelper;
import beans.BeanManager;
import blog.data.EIBlogCategory;
import blog.service.IBlogCategoryService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;

public class SetBlogCategoryCtrl extends WithMatcher {

    private IBlogCategoryService blogCategoryService = BeanManager.getInstance().getService(IBlogCategoryService.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "blog set category";
    }

    @Override
    public String urlPattern() {
        return "/blog/category/set";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        if (!RestHelper.isPost(httpRequest, httpResponse)) {
            return;
        }

        EIBlogCategory blogCategory = RestHelper.getBodyAsObject(httpRequest, EIBlogCategory.class);
        RestHelper.oneCallAndRet(httpResponse, this.blogCategoryService, "save", blogCategory);
    }
}
