package blog.controller;

import auth.AuthHelper;
import beans.BeanManager;
import blog.service.IBlogCommentService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.RestCode;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;
import java.util.Map;

public class ListAllBlogCommentByPostCtrl extends WithMatcher {

    private IBlogCommentService blogCommentService = BeanManager.getInstance().getService(IBlogCommentService.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "blog list all blog comment by post";
    }

    @Override
    public String urlPattern() {
        return "/blog/comment/all/:postId";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        Map<String, String> params = this.getUriPatternMatcher().getParams(httpRequest);
        Long postId = null;
        try {
            postId = Long.parseLong(params.get("postId"));
        } catch (NumberFormatException e) {
            RestHelper.catching(e, httpResponse, RestCode.GENERAL_ERROR);
            return;
        }

        RestHelper.oneCallAndRet(httpResponse, this.blogCommentService, "listAllByPostId", postId);
    }
}
