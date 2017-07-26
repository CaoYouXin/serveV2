package meta.controller;

import auth.AuthHelper;
import beans.BeanManager;
import meta.service.IResourceService;
import meta.service.impl.ResourceServiceImpl;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.JsonResponse;
import rest.RestCode;
import rest.RestHelper;
import rest.WithMatcher;
import util.BashUtil;

import java.io.IOException;
import java.util.Map;

public class UnzipCtrl extends WithMatcher {

    static {
        BeanManager.getInstance().setService(IResourceService.class, ResourceServiceImpl.class);
    }

    private IResourceService resourceService = BeanManager.getInstance().getService(IResourceService.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "meta unzip";
    }

    @Override
    public String urlPattern() {
        return "/unzip/:path?to=:to";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        Map<String, String> params = this.getUriPatternMatcher().getParams(request);
        String path = params.get("path"), to = params.get("to");

        String transformedPath = null, transformedTo = null;
        try {
            transformedPath = this.resourceService.transformFromPath(path);
            transformedTo = this.resourceService.transformFromPath(to);
        } catch (Throwable e) {
            RestHelper.catching(e, response, RestCode.GENERAL_ERROR);
            return;
        }

        BashUtil.run("unzip -qo " + transformedPath + " -d " + transformedTo, false);
        RestHelper.responseJSON(response, JsonResponse.success("已执行解压命令【 unzip -qo zipFilePath -d dirPath 】."));
    }
}
