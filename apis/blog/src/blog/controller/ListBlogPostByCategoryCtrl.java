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
import java.util.Map;

public class ListBlogPostByCategoryCtrl extends WithMatcher {

    private IBlogPostService blogPostService = BeanManager.getInstance().getService(IBlogPostService.class);

    @Override
    public String name() {
        return "blog list all posts by category";
    }

    @Override
    public String urlPattern() {
        return "/blog/post/list/client/:categoryId";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        Map<String, String> params = this.getUriPatternMatcher().getParams(httpRequest);
        Long categoryId = Long.parseLong(params.get("categoryId"));

        RestHelper.oneCallAndRet(httpResponse, this.blogPostService, "listByCategory", categoryId);
    }
}
