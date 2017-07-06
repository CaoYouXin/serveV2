package meta.controller;

import beans.BeanManager;
import meta.service.IAdminService;
import meta.service.exp.AdminSettingException;
import meta.service.impl.AdminServiceImpl;
import meta.view.EIAdminSetting;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.HelperController;
import rest.JsonResponse;
import rest.RestHelper;

import java.io.IOException;

public class AdminSettingCtrl extends HelperController {

    static {
        BeanManager.getInstance().setService(IAdminService.class, AdminServiceImpl.class);
    }

    private IAdminService adminService = BeanManager.getInstance().getService(IAdminService.class);

    @Override
    public String name() {
        return "meta admin setting";
    }

    @Override
    public String urlPattern() {
        return "/admin/setting";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        if (!RestHelper.isPost(request, response)) {
            return;
        }

        EIAdminSetting adminSetting = RestHelper.getBodyAsObject(request, EIAdminSetting.class);
        try {
            Boolean suc = this.adminService.setting(
                    adminSetting.getOldUserName(),
                    adminSetting.getUserName(),
                    adminSetting.getOldPassword(),
                    adminSetting.getPassword()
            );
            RestHelper.responseJSON(response, JsonResponse.success(suc));
        } catch (AdminSettingException e) {
            RestHelper.responseJSON(response, JsonResponse.fail(50001, e.getMessage()));
        }
    }
}
