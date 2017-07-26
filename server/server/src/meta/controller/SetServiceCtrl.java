package meta.controller;

import auth.AuthHelper;
import beans.BeanManager;
import meta.data.EIService;
import meta.service.IServiceService;
import meta.service.impl.ServiceServiceImpl;
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

public class SetServiceCtrl extends WithMatcher {

    static {
        BeanManager.getInstance().setService(IServiceService.class, ServiceServiceImpl.class);
    }

    private IServiceService serviceService = BeanManager.getInstance().getService(IServiceService.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "meta set service";
    }

    @Override
    public String urlPattern() {
        return "/service/set/:intf/:impl";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        Map<String, String> params = this.getUriPatternMatcher().getParams(request);
        String intfClassName = params.get("intf");
        String implClassName = params.get("impl");

        EIService service = null;
        try {
            service = this.serviceService.setService(intfClassName, implClassName);
        } catch (Throwable e) {
            RestHelper.catching(e, response, RestCode.GENERAL_ERROR);
            return;
        }

        RestHelper.responseJSON(response, JsonResponse.success(service));
    }
}
