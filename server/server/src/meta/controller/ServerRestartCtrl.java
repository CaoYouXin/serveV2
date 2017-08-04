package meta.controller;

import auth.AuthHelper;
import config.Configs;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.JsonResponse;
import rest.RestHelper;
import rest.WithMatcher;
import util.BashUtil;

import java.io.IOException;
import java.util.Map;

public class ServerRestartCtrl extends WithMatcher {

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "meta server restart";
    }

    @Override
    public String urlPattern() {
        return "/server/restart/:downCount";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        Map<String, String> params = this.getUriPatternMatcher().getParams(request);
        String downCount = params.get("downCount");

        String jarFilepath = Configs.getConfigs(Configs.JAR_FILE_PATH, String.class);
        BashUtil.run(String.format("nohup java -jar %s restart " + downCount, jarFilepath), false);
        RestHelper.responseJSON(response, JsonResponse.success(Math.min(10, Integer.parseInt(downCount)) + 5));
    }
}
