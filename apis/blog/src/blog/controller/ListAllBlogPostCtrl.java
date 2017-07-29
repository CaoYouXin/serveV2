package blog.controller;

import auth.AuthHelper;
import beans.BeanManager;
import blog.service.IBlogPostService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;

public class ListAllBlogPostCtrl extends WithMatcher {

    private IBlogPostService blogPostService = BeanManager.getInstance().getService(IBlogPostService.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "blog list all posts";
    }

    @Override
    public String urlPattern() {
        return "/blog/post/list/all";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        RestHelper.oneCallAndRet(httpResponse, this.blogPostService, "list");
    }
}
