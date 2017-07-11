package meta.controller;

import auth.AuthHelper;
import beans.BeanManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.Configs;
import config.DataSourceConfig;
import meta.service.IDataSourceService;
import meta.service.IDatabaseStatusService;
import meta.service.impl.DataSourceServiceImpl;
import meta.service.impl.DatabaseStatusServiceImpl;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rest.Controller;
import rest.HelperController;
import rest.JsonResponse;
import rest.RestHelper;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class DataSourceActiveCtrl extends HelperController implements Controller {

    private static final Logger logger = LogManager.getLogger(DataSourceActiveCtrl.class);

    static {
        BeanManager.getInstance().setService(IDatabaseStatusService.class, DatabaseStatusServiceImpl.class);
        BeanManager.getInstance().setService(IDataSourceService.class, DataSourceServiceImpl.class);
    }

    private IDatabaseStatusService databaseStatusService = BeanManager.getInstance().getService(IDatabaseStatusService.class);
    private IDataSourceService dataSourceService = BeanManager.getInstance().getService(IDataSourceService.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN | AuthHelper.START_LOG;
    }

    @Override
    public String name() {
        return "meta DataSource Active";
    }

    @Override
    public String urlPattern() {
        return "/database/init/:schema";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        Map<String, String> params = this.getUriPatternMatcher().getParams(request);
        String schema = params.get("schema");

        File[] cfgFiles = this.databaseStatusService.listCfgFiles();
        if (null != cfgFiles) {
            for (File cfgFile : cfgFiles) {
                if (!cfgFile.getAbsolutePath().endsWith(schema + ".json")) {
                    continue;
                }

                if (this.databaseStatusService.cpCfg2Active(cfgFile)) {
                    Boolean testCfgConn = this.databaseStatusService.testCfgConn(cfgFile);
                    RestHelper.responseJSON(response, JsonResponse.success(testCfgConn));
                    return;
                }
            }
        }

        DataSourceConfig dataSourceConfig = null;
        try {
            dataSourceConfig = this.dataSourceService.newDataSourceConfig(schema);
        } catch (SQLException e) {
            logger.catching(e);
            RestHelper.responseJSON(response, JsonResponse.fail(50000, e.getMessage()));
            return;
        }

        ObjectMapper objectMapper = Configs.getConfigs(Configs.OBJECT_MAPPER, ObjectMapper.class);
        if (this.databaseStatusService.cpCfg2Active(objectMapper.writeValueAsString(dataSourceConfig), schema)) {
            Boolean testCfgConn = this.databaseStatusService.testCfgConn(this.databaseStatusService.activeCfgFile());
            RestHelper.responseJSON(response, JsonResponse.success(testCfgConn));
        }
    }
}
