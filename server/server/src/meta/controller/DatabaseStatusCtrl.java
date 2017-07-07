package meta.controller;

import beans.BeanManager;
import meta.service.IDatabaseStatusService;
import meta.service.impl.DatabaseStatusServiceImpl;
import meta.view.EIDatabaseStatus;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.Controller;
import rest.HelperController;
import rest.JsonResponse;
import rest.RestHelper;

import java.io.File;
import java.io.IOException;

public class DatabaseStatusCtrl extends HelperController implements Controller {

    static {
        BeanManager.getInstance().setService(IDatabaseStatusService.class, DatabaseStatusServiceImpl.class);
    }

    private IDatabaseStatusService databaseStatusService = BeanManager.getInstance().getService(IDatabaseStatusService.class);

    @Override
    public String name() {
        return "meta database status";
    }

    @Override
    public String urlPattern() {
        return "/database/status";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        EIDatabaseStatus databaseStatus = BeanManager.getInstance().createBean(EIDatabaseStatus.class);

        File[] files = this.databaseStatusService.listCfgFiles();
        if (null == files || files.length == 0) {
            databaseStatus.setHasConfigs(false);
            RestHelper.responseJSON(response, JsonResponse.success(databaseStatus));
            return;
        }
        databaseStatus.setHasConfigs(true);

        File activeCfgFile = this.databaseStatusService.activeCfgFile();
        if (null == activeCfgFile) {
            databaseStatus.setHasActiveConfig(false);
            RestHelper.responseJSON(response, JsonResponse.success(databaseStatus));
            return;
        }
        databaseStatus.setHasActiveConfig(true);

        Boolean testCfgConn = this.databaseStatusService.testCfgConn(activeCfgFile);
        databaseStatus.setActiveDatasource(testCfgConn);
        RestHelper.responseJSON(response, JsonResponse.success(databaseStatus));
    }
}
