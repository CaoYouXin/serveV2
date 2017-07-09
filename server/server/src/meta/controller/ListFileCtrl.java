package meta.controller;

import auth.AuthHelper;
import beans.BeanManager;
import config.Configs;
import config.InitConfig;
import meta.service.IListFileService;
import meta.service.impl.ListFileServiceImpl;
import meta.view.EIFileInfo;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.HelperController;
import rest.JsonResponse;
import rest.RestHelper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListFileCtrl extends HelperController {

    private static final Map<String, String> CONFIGS = new HashMap<>();

    static {
        BeanManager.getInstance().setService(IListFileService.class, ListFileServiceImpl.class);

        InitConfig initConfig = Configs.getConfigs(InitConfig.CONFIG_KEY, InitConfig.class);
        CONFIGS.put("classpath", initConfig.getClasspath());
        CONFIGS.put("config", initConfig.getConfigRoot());
        CONFIGS.put("serve", initConfig.getResourceRoot());
        CONFIGS.put("upload", initConfig.getUploadRoot());
    }

    private IListFileService listFileService = BeanManager.getInstance().getService(IListFileService.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "meta list file";
    }

    @Override
    public String urlPattern() {
        return "/list/:path";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        Map<String, String> params = this.getUriPatternMatcher().getParams(request);
        String path = params.get("path");
        int beginIndex = path.indexOf('/');
        String head = path.substring(0, beginIndex);
        if (CONFIGS.containsKey(head)) {
            List<EIFileInfo> children = this.listFileService.getChildren(new File(
                    CONFIGS.get(head), path.substring(beginIndex)
            ));
            RestHelper.responseJSON(response, JsonResponse.success(children));
            return;
        }

        RestHelper.responseJSON(response, JsonResponse.fail(50003, "路径头不正确."));
    }
}
