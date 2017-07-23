package meta.controller;

import auth.AuthHelper;
import beans.BeanManager;
import meta.data.EIInterceptor;
import meta.service.IInterceptorService;
import meta.service.impl.InterceptorServiceImpl;
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

public class SetInterceptorCtrl extends WithMatcher {

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
        return "meta set interceptor";
    }

    @Override
    public String urlPattern() {
        return "/interceptor/set/:className";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        Map<String, String> params = this.getUriPatternMatcher().getParams(request);
        String className = params.get("className");

        EIInterceptor interceptor = null;
        try {
            interceptor = this.interceptorService.save(className);
        } catch (Throwable e) {
            RestHelper.catching(e, response, RestCode.GENERAL_ERROR);
            return;
        }

        RestHelper.responseJSON(response, JsonResponse.success(interceptor));
    }
}
