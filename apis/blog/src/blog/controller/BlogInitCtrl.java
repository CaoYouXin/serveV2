package blog.controller;

import auth.AuthHelper;
import beans.BeanManager;
import blog.auth.AuthHelperExt;
import meta.data.EIAuth;
import meta.service.IAuthService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.JsonResponse;
import rest.RestCode;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;
import java.util.List;

public class BlogInitCtrl extends WithMatcher {

    private IAuthService authService = BeanManager.getInstance().getService(IAuthService.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "blog init";
    }

    @Override
    public String urlPattern() {
        return "/blog/init";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        List<EIAuth> authList = AuthHelperExt.addExtAuth();
        Boolean ret = this.authService.setAuths(authList);
        RestHelper.responseJSON(httpResponse, ret ? JsonResponse.success("操作成功！")
                : JsonResponse.fail(RestCode.GENERAL_ERROR, "操作失败！"));
    }
}
