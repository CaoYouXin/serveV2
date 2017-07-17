package blog.controller;

import auth.AuthHelper;
import beans.BeanManager;
import blog.service.IUserService;
import blog.service.exp.UserException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.HelperController;
import rest.JsonResponse;
import rest.RestCode;
import rest.RestHelper;
import util.BoolUtil;

import java.io.IOException;
import java.util.Map;

public class UserDisabledCtrl extends HelperController {

    private IUserService userService = BeanManager.getInstance().getService(IUserService.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "blog user disabled";
    }

    @Override
    public String urlPattern() {
        return "/blog/user/disabled/:id/:disabled";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        Map<String, String> params = this.getUriPatternMatcher().getParams(httpRequest);
        Long id = Long.parseLong(params.get("id"));
        String disabledStr = params.get("disabled");
        Boolean disabled = BoolUtil.parseTF(disabledStr);
        if (null == disabled) {
            RestHelper.responseJSON(httpResponse, JsonResponse.fail(RestCode.GENERAL_ERROR, "参数不正确"));
            return;
        }

        Boolean trulyDisabled = null;
        try {
            trulyDisabled = this.userService.disable(id, disabled);
        } catch (Throwable e) {
            RestHelper.catching(e, httpResponse, RestCode.GENERAL_ERROR);
            return;
        }
        RestHelper.responseJSON(httpResponse, JsonResponse.success(trulyDisabled));
    }
}
