package meta.controller;

import beans.BeanManager;
import meta.service.IFileService;
import meta.service.IResourceService;
import meta.service.impl.FileServiceImpl;
import meta.service.impl.ResourceServiceImpl;
import meta.view.EIFileCopy;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.JsonResponse;
import rest.RestCode;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;
import java.util.ArrayList;

public class CopyFilesCtrl extends WithMatcher {

    static {
        BeanManager.getInstance().setService(IFileService.class, FileServiceImpl.class);
        BeanManager.getInstance().setService(IResourceService.class, ResourceServiceImpl.class);
    }

    private IFileService fileService = BeanManager.getInstance().getService(IFileService.class);
    private IResourceService resourceService = BeanManager.getInstance().getService(IResourceService.class);


    @Override
    public int auth() {
        return 0;
    }

    @Override
    public String name() {
        return "copy files";
    }

    @Override
    public String urlPattern() {
        return "/copy";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        if (!RestHelper.isPost(request, response)) {
            return;
        }

        EIFileCopy fileCopy = RestHelper.getBodyAsObject(request, EIFileCopy.class);
        EIFileCopy transformedFileCopy = BeanManager.getInstance().createBean(EIFileCopy.class);

        Boolean copyRet = null;
        try {
            transformedFileCopy.setDst(this.resourceService.transformFromPath(fileCopy.getDst()));
            for (String src : fileCopy.getSrc()) {
                if (null == transformedFileCopy.getSrc()) {
                    transformedFileCopy.setSrc(new ArrayList<>());
                }

                transformedFileCopy.getSrc().add(this.resourceService.transformFromPath(src));
            }
            copyRet = this.fileService.copy(transformedFileCopy.getSrc(), transformedFileCopy.getDst());
        } catch (Throwable e) {
            RestHelper.catching(e, response, RestCode.GENERAL_ERROR);
            return;
        }

        RestHelper.responseJSON(response, JsonResponse.success(copyRet ? "操作成功" : "操作失败"));
    }
}
