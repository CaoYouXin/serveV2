package meta.controller;

import auth.AuthHelper;
import beans.BeanManager;
import meta.service.IFileService;
import meta.service.IResourceService;
import meta.service.impl.FileServiceImpl;
import meta.service.impl.ResourceServiceImpl;
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
import java.util.Map;

public class CreateDirCtrl extends WithMatcher {

    static {
        BeanManager.getInstance().setService(IFileService.class, FileServiceImpl.class);
        BeanManager.getInstance().setService(IResourceService.class, ResourceServiceImpl.class);
    }

    private IFileService fileService = BeanManager.getInstance().getService(IFileService.class);
    private IResourceService resourceService = BeanManager.getInstance().getService(IResourceService.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "create dir";
    }

    @Override
    public String urlPattern() {
        return "/create/:path";
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

        Boolean createRet = this.fileService.createDir(new File(transformed));
        RestHelper.responseJSON(response, JsonResponse.success(createRet ? "操作成功" : "操作失败"));
    }
}
