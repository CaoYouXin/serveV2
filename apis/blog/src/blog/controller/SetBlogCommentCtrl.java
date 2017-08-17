package blog.controller;

import auth.AuthHelper;
import beans.BeanManager;
import blog.auth.AuthHelperExt;
import blog.data.EIComment;
import blog.service.IBlogCommentService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;

public class SetBlogCommentCtrl extends WithMatcher {

    private IBlogCommentService blogCommentService = BeanManager.getInstance().getService(IBlogCommentService.class);

    @Override
    public int auth() {
        return AuthHelperExt.BLOG_LOGIN | AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "blog set post's comment";
    }

    @Override
    public String urlPattern() {
        return "/blog/comment/set";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        if (!RestHelper.isPost(httpRequest, httpResponse)) {
            return;
        }

        EIComment comment = RestHelper.getBodyAsObject(httpRequest, EIComment.class);
        RestHelper.oneCallAndRet(httpResponse, this.blogCommentService, "save", comment);
    }
}
