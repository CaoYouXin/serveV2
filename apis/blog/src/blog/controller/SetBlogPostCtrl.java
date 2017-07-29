package blog.controller;

import auth.AuthHelper;
import beans.BeanManager;
import blog.data.EIBlogPost;
import blog.service.IBlogPostService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;

public class SetBlogPostCtrl extends WithMatcher {

    private IBlogPostService blogPostService = BeanManager.getInstance().getService(IBlogPostService.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "blog set post";
    }

    @Override
    public String urlPattern() {
        return "/blog/post/set";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        if (!RestHelper.isPost(httpRequest, httpResponse)) {
            return;
        }

        EIBlogPost blogPost = RestHelper.getBodyAsObject(httpRequest, EIBlogPost.class);
        RestHelper.oneCallAndRet(httpResponse, this.blogPostService, "save", blogPost);
    }
}
