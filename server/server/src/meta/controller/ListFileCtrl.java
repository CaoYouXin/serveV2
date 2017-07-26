package meta.controller;

import auth.AuthHelper;
import beans.BeanManager;
import meta.service.IListFileService;
import meta.service.IResourceService;
import meta.service.impl.ListFileServiceImpl;
import meta.service.impl.ResourceServiceImpl;
import meta.view.EIFileInfo;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.JsonResponse;
import rest.RestCode;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ListFileCtrl extends WithMatcher {

    static {
        BeanManager.getInstance().setService(IListFileService.class, ListFileServiceImpl.class);
        BeanManager.getInstance().setService(IResourceService.class, ResourceServiceImpl.class);
    }

    private IListFileService listFileService = BeanManager.getInstance().getService(IListFileService.class);
    private IResourceService resourceService = BeanManager.getInstance().getService(IResourceService.class);

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


        String transformed = null;
        try {
            transformed = this.resourceService.transformFromPath(path);
        } catch (Throwable e) {
            RestHelper.catching(e, response, RestCode.GENERAL_ERROR);
            return;
        }

        List<EIFileInfo> children = this.listFileService.getChildren(new File(transformed));
        RestHelper.responseJSON(response, JsonResponse.success(children));
    }
}
