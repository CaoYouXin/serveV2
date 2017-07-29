package blog.controller;

import beans.BeanManager;
import blog.service.IBlogCommentService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;
import java.util.Map;

public class ListBlogCommentByPostCtrl extends WithMatcher {

    private IBlogCommentService blogCommentService = BeanManager.getInstance().getService(IBlogCommentService.class);

    @Override
    public String name() {
        return "blog list post's comment";
    }

    @Override
    public String urlPattern() {
        return "/blog/comment/:postId";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        Map<String, String> params = this.getUriPatternMatcher().getParams(httpRequest);
        Long postId = Long.parseLong(params.get("postId"));

        RestHelper.oneCallAndRet(httpResponse, this.blogCommentService, "listByPostId", postId);
    }
}
