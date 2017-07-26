package meta.controller;

import auth.AuthHelper;
import beans.BeanManager;
import meta.service.IControllerService;
import meta.service.impl.ControllerServiceImpl;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.JsonResponse;
import rest.RestCode;
import rest.RestHelper;
import rest.WithMatcher;
import util.BoolUtil;

import java.io.IOException;
import java.util.Map;

public class DisableControllerCtrl extends WithMatcher {

    static {
        BeanManager.getInstance().setService(IControllerService.class, ControllerServiceImpl.class);
    }

    private IControllerService controllerService = BeanManager.getInstance().getService(IControllerService.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "meta disable controller";
    }

    @Override
    public String urlPattern() {
        return "/controller/set/disabled/:id/:disabled";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        Map<String, String> params = this.getUriPatternMatcher().getParams(request);
        Long id = Long.parseLong(params.get("id"));
        String disabledStr = params.get("disabled");
        Boolean disabled = BoolUtil.parseTF(disabledStr);
        if (null == disabled) {
            RestHelper.responseJSON(response, JsonResponse.fail(RestCode.GENERAL_ERROR, "参数不正确"));
            return;
        }

        Boolean trulyDisabled = null;
        try {
            trulyDisabled = this.controllerService.setControllerDisabled(id, disabled);
        } catch (Throwable e) {
            RestHelper.catching(e, response, RestCode.GENERAL_ERROR);
            return;
        }

        RestHelper.responseJSON(response, JsonResponse.success(trulyDisabled));
    }
}
