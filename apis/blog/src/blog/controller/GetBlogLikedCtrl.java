package blog.controller;

import beans.BeanManager;
import blog.service.IBlogLikeService;
import blog.service.IUserService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;
import java.util.Map;

public class GetBlogLikedCtrl extends WithMatcher {

    private IBlogLikeService blogLikeService = BeanManager.getInstance().getService(IBlogLikeService.class);
    private IUserService userService = BeanManager.getInstance().getService(IUserService.class);

    @Override
    public String name() {
        return "blog get blog liked";
    }

    @Override
    public String urlPattern() {
        return "/blog/liked/:postId";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        Map<String, String> params = this.getUriPatternMatcher().getParams(httpRequest);
        Long postId = Long.parseLong(params.get("postId"));

        Long userId = this.userService.getUserIdFromRequest(httpRequest);

        RestHelper.oneCallAndRet(httpResponse, this.blogLikeService, "liked", userId, postId);
    }
}
