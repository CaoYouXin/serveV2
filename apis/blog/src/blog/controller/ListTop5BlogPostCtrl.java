package blog.controller;

import beans.BeanManager;
import blog.service.IBlogPostService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;

public class ListTop5BlogPostCtrl extends WithMatcher {

    private IBlogPostService blogPostService = BeanManager.getInstance().getService(IBlogPostService.class);

    @Override
    public String name() {
        return "blog list top 5 posts";
    }

    @Override
    public String urlPattern() {
        return "/blog/post/list/top5";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        RestHelper.oneCallAndRet(httpResponse, this.blogPostService, "top5");
    }
}
