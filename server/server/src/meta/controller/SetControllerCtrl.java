package meta.controller;

import auth.AuthHelper;
import beans.BeanManager;
import meta.data.EIController;
import meta.service.IControllerService;
import meta.service.impl.ControllerServiceImpl;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.WithMatcher;
import rest.JsonResponse;
import rest.RestCode;
import rest.RestHelper;

import java.io.IOException;
import java.util.Map;

public class SetControllerCtrl extends WithMatcher {

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
        return "meta set controller";
    }

    @Override
    public String urlPattern() {
        return "/controller/set/:className";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        Map<String, String> params = this.getUriPatternMatcher().getParams(request);
        String className = params.get("className");

        EIController controller = null;
        try {
            controller = this.controllerService.setController(className);
        } catch (Throwable e) {
            RestHelper.catching(e, response, RestCode.GENERAL_ERROR);
            return;
        }

        RestHelper.responseJSON(response, JsonResponse.success(controller));
    }
}
