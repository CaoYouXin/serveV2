package blog.controller;

import beans.BeanManager;
import blog.service.IUserService;
import blog.view.EIRegisterUser;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.HelperController;
import rest.JsonResponse;
import rest.RestCode;
import rest.RestHelper;

import java.io.IOException;

public class UserRegisterCtrl extends HelperController {

    private IUserService userService = BeanManager.getInstance().getService(IUserService.class);

    @Override
    public String name() {
        return "blog user register";
    }

    @Override
    public String urlPattern() {
        return "/blog/user/register";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        if (!RestHelper.isPost(request, response)) {
            return;
        }

        EIRegisterUser eiRegisterUser = RestHelper.getBodyAsObject(request, EIRegisterUser.class);
        if (null == eiRegisterUser) {
            RestHelper.responseJSON(response, JsonResponse.fail(RestCode.GENERAL_ERROR, "参数不正确."));
            return;
        }

        Boolean register = null;
        try {
            register = this.userService.register(eiRegisterUser);
        } catch (Throwable e) {
            RestHelper.catching(e, response, RestCode.GENERAL_ERROR);
            return;
        }

        RestHelper.responseJSON(response, JsonResponse.success(register));
    }
}
