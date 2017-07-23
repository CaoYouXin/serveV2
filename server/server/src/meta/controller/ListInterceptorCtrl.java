package meta.controller;

import auth.AuthHelper;
import beans.BeanManager;
import meta.data.EIController;
import meta.data.EIInterceptor;
import meta.service.IControllerService;
import meta.service.IInterceptorService;
import meta.service.impl.ControllerServiceImpl;
import meta.service.impl.InterceptorServiceImpl;
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

public class ListInterceptorCtrl extends HelperController {

    static {
        BeanManager.getInstance().setService(IInterceptorService.class, InterceptorServiceImpl.class);
    }

    private IInterceptorService interceptorService = BeanManager.getInstance().getService(IInterceptorService.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "meta list interceptor";
    }

    @Override
    public String urlPattern() {
        return "/interceptor/list";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        List<EIInterceptor> eiInterceptors = null;
        try {
            eiInterceptors = this.interceptorService.list();
        } catch (Throwable e) {
            RestHelper.catching(e, response, RestCode.GENERAL_ERROR);
            return;
        }
        RestHelper.responseJSON(response, JsonResponse.success(eiInterceptors));
    }
}
