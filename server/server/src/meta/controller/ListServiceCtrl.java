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
import rest.HelperController;
import rest.JsonResponse;
import rest.RestHelper;

import java.io.IOException;
import java.util.List;

public class ListServiceCtrl extends HelperController {

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
        return "meta list service";
    }

    @Override
    public String urlPattern() {
        return "/service/list";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        List<EIService> eiServices = null;
        try {
            eiServices = this.serviceService.listServices();
        } catch (Throwable e) {
            RestHelper.catching(e, response, 50005);
            return;
        }
        RestHelper.responseJSON(response, JsonResponse.success(eiServices));
    }
}
