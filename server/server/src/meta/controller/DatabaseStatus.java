package meta.controller;

import beans.BeanManager;
import config.Configs;
import config.InitConfig;
import meta.entities.EIDatabaseStatus;
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

public class DatabaseStatus extends HelperController implements Controller {
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

        InitConfig initConfig = Configs.getConfigs(InitConfig.CONFIG_KEY, InitConfig.class);
        File databaseConfigDir = new File(initConfig.getConfigRoot(), "dbcfgs");
        if (!databaseConfigDir.exists()) {
            databaseStatus.setHasConfigs(false);
            RestHelper.responseJSON(response, JsonResponse.success(databaseStatus));
            return;
        }
        databaseStatus.setHasConfigs(true);

        File activeDatabaseConfig = new File(initConfig.getConfigRoot(), "dbcfgs/active/db.json");
        if (!activeDatabaseConfig.exists()) {

        }
    }
}
