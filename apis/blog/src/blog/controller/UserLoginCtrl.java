package blog.controller;

import beans.BeanManager;
import blog.data.EIUser;
import blog.service.IUserService;
import blog.view.EILoginUser;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.JsonResponse;
import rest.RestCode;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;

public class UserLoginCtrl extends WithMatcher {

    private IUserService userService = BeanManager.getInstance().getService(IUserService.class);

    @Override
    public String name() {
        return "blog user login";
    }

    @Override
    public String urlPattern() {
        return "/blog/user/login";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext httpContext) throws HttpException, IOException {
        if (!RestHelper.isPost(request, response)) {
            return;
        }

        EIUser eiUser = RestHelper.getBodyAsObject(request, EIUser.class);
        if (null == eiUser) {
            RestHelper.responseJSON(response, JsonResponse.fail(RestCode.GENERAL_ERROR, "参数不正确."));
            return;
        }

        EILoginUser eiLoginUser = null;
        try {
            eiLoginUser = this.userService.login(eiUser);
        } catch (Throwable e) {
            RestHelper.catching(e, response, RestCode.GENERAL_ERROR);
            return;
        }

        RestHelper.responseJSON(response, JsonResponse.success(eiLoginUser));
    }
}
