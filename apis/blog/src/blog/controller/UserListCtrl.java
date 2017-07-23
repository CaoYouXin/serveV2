package blog.controller;

import auth.AuthHelper;
import beans.BeanManager;
import blog.data.EIUser;
import blog.service.IUserService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.HelperController;
import rest.JsonResponse;
import rest.RestCode;
import rest.RestHelper;

import java.io.IOException;
import java.util.List;

public class UserListCtrl extends HelperController {

    private IUserService userService = BeanManager.getInstance().getService(IUserService.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "blog user list";
    }

    @Override
    public String urlPattern() {
        return "/blog/user/list";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        List<EIUser> eiUsers = null;
        try {
            eiUsers = this.userService.listUsers();
        } catch (Throwable e) {
            RestHelper.catching(e, httpResponse, RestCode.GENERAL_ERROR);
        }
        RestHelper.responseJSON(httpResponse, JsonResponse.success(eiUsers));
    }
}
