package blog.controller;

import beans.BeanManager;
import blog.service.IBlogPostService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.RestCode;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;
import java.util.Map;

public class GetPostByIdCtrl extends WithMatcher {

    private IBlogPostService blogPostService = BeanManager.getInstance().getService(IBlogPostService.class);

    @Override
    public String name() {
        return "blog get post by id";
    }

    @Override
    public String urlPattern() {
        return "/blog/post/:id";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        Map<String, String> params = this.getUriPatternMatcher().getParams(httpRequest);

        Long id = null;
        try {
            id = Long.parseLong(params.get("id"));
        } catch (NumberFormatException e) {
            RestHelper.catching(e, httpResponse, RestCode.GENERAL_ERROR);
            return;
        }

        RestHelper.oneCallAndRet(httpResponse, this.blogPostService, "getById", id);
    }
}
