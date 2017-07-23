package meta.controller;

import beans.BeanManager;
import config.Configs;
import config.DataSourceConfig;
import config.InitConfig;
import meta.service.IDatabaseStatusService;
import meta.service.impl.DatabaseStatusServiceImpl;
import meta.view.EIDatabaseStatus;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.Controller;
import rest.WithMatcher;
import rest.JsonResponse;
import rest.RestHelper;
import util.FileUtil;

import java.io.File;
import java.io.IOException;

public class DatabaseStatusCtrl extends WithMatcher implements Controller {

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
            RestHelper.responseJSON(response, JsonResponse.success(databaseStatus));
            return;
        }
        String[] configs = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            configs[i] = files[i].getName().replaceAll("\\.json", "");
        }
        databaseStatus.setConfigs(configs);

        File activeCfgFile = this.databaseStatusService.activeCfgFile();
        if (null == activeCfgFile) {
            RestHelper.responseJSON(response, JsonResponse.success(databaseStatus));
            return;
        }
        int length = Configs.getConfigs(InitConfig.CONFIG_KEY, InitConfig.class).getDataSource().getUrl().length();
        databaseStatus.setActiveConfig(
                FileUtil.getObjectFromFile(activeCfgFile, DataSourceConfig.class).getUrl()
                        .substring(length).replaceAll("\\/", "")
        );

        Boolean testCfgConn = this.databaseStatusService.testCfgConn(activeCfgFile);
        databaseStatus.setActiveDatasource(testCfgConn);
        RestHelper.responseJSON(response, JsonResponse.success(databaseStatus));
    }
}
