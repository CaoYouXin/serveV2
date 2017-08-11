package blog.controller;

import beans.BeanManager;
import blog.auth.AuthHelperExt;
import blog.auth.BlogLoginAuth;
import blog.service.IBlogLikeService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.JsonResponse;
import rest.RestCode;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;
import java.util.Map;

public class SetBlogLikeCtrl extends WithMatcher {

    private IBlogLikeService blogLikeService = BeanManager.getInstance().getService(IBlogLikeService.class);

    @Override
    public int auth() {
        return AuthHelperExt.BLOG_LOGIN;
    }

    @Override
    public String name() {
        return "blog set like";
    }

    @Override
    public String urlPattern() {
        return "/blog/like/:postId";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        Map<String, String> params = this.getUriPatternMatcher().getParams(httpRequest);
        Long postId = Long.parseLong(params.get("postId"));

        Long userId = (Long) httpContext.getAttribute(BlogLoginAuth.USER_ID_KEY);
        if (null == userId) {
            RestHelper.responseJSON(httpResponse, JsonResponse.fail(RestCode.UNAUTHED, "没有用户信息"));
            return;
        }

        RestHelper.oneCallAndRet(httpResponse, this.blogLikeService, "like", postId, userId);
    }
}
