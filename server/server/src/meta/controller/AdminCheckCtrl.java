package meta.controller;

import beans.BeanManager;
import meta.service.IAdminService;
import meta.service.impl.AdminServiceImpl;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.Controller;
import rest.JsonResponse;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;

public class AdminCheckCtrl extends WithMatcher implements Controller {

    static {
        BeanManager.getInstance().setService(IAdminService.class, AdminServiceImpl.class);
    }

    private IAdminService adminService = BeanManager.getInstance().getService(IAdminService.class);

    @Override
    public String name() {
        return "meta admin check";
    }

    @Override
    public String urlPattern() {
        return "/admin/check";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        if (!RestHelper.isGet(request, response)) {
            return;
        }

        RestHelper.responseJSON(response, JsonResponse.success(this.adminService.check()));
    }
}
