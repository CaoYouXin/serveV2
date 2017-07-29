package blog.controller;

import beans.BeanManager;
import blog.service.IBlogLikeService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;
import java.util.Map;

public class GetBlogLikeCountCtrl extends WithMatcher {

    private IBlogLikeService blogLikeService = BeanManager.getInstance().getService(IBlogLikeService.class);

    @Override
    public String name() {
        return "blog get like count";
    }

    @Override
    public String urlPattern() {
        return "/blog/like/count/:postId";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        Map<String, String> params = this.getUriPatternMatcher().getParams(httpRequest);
        Long postId = Long.parseLong(params.get("postId"));

        RestHelper.oneCallAndRet(httpResponse, this.blogLikeService, "likeCount", postId);
    }
}
