package meta.controller;

import auth.AuthHelper;
import config.Configs;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.WithMatcher;
import rest.JsonResponse;
import rest.RestHelper;
import util.BashUtil;

import java.io.IOException;

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
        return "/server/restart";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        String jarFilepath = Configs.getConfigs(Configs.JAR_FILE_PATH, String.class);
        BashUtil.run(String.format("nohup java -jar %s restart", jarFilepath), false);
        RestHelper.responseJSON(response, JsonResponse.success(125));
    }
}
