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
import rest.HelperController;
import rest.JsonResponse;
import rest.RestHelper;

import java.io.IOException;
import java.util.List;

public class ListControllerCtrl extends HelperController {

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
        return "meta list controller";
    }

    @Override
    public String urlPattern() {
        return "/controller/list";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        List<EIController> controllers = controllerService.listControllers();
        RestHelper.responseJSON(response, JsonResponse.success(controllers));
    }
}
