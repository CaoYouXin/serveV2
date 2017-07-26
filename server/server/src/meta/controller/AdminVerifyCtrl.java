package meta.controller;

import beans.BeanManager;
import meta.service.IAdminService;
import meta.service.impl.AdminServiceImpl;
import meta.view.EIAdminVerify;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.JsonResponse;
import rest.RestCode;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;

public class AdminVerifyCtrl extends WithMatcher {

    static {
        BeanManager.getInstance().setService(IAdminService.class, AdminServiceImpl.class);
    }

    private IAdminService adminService = BeanManager.getInstance().getService(IAdminService.class);

    @Override
    public String name() {
        return "meta admin verify";
    }

    @Override
    public String urlPattern() {
        return "/admin/verify";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        if (!RestHelper.isPost(request, response)) {
            return;
        }

        EIAdminVerify adminVerify = RestHelper.getBodyAsObject(request, EIAdminVerify.class);
        try {
            String token = this.adminService.verify(adminVerify.getUserName(), adminVerify.getPassword());
            RestHelper.responseJSON(response, JsonResponse.success(token));
        } catch (Throwable e) {
            RestHelper.catching(e, response, RestCode.GENERAL_ERROR);
        }
    }
}
