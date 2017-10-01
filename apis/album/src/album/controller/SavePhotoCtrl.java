package album.controller;

import album.data.EIAlbumPhoto;
import album.service.IAlbumService;
import beans.BeanManager;
import blog.auth.AuthHelperExt;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.JsonResponse;
import rest.RestCode;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;

public class SavePhotoCtrl extends WithMatcher {

    private IAlbumService albumService = BeanManager.getInstance().getService(IAlbumService.class);

    @Override
    public int auth() {
        return AuthHelperExt.BLOG_LOGIN;
    }

    @Override
    public String name() {
        return "album save a photo";
    }

    @Override
    public String urlPattern() {
        return "/album/photo/save";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        if (!RestHelper.isPost(httpRequest, httpResponse)) {
            return;
        }

        EIAlbumPhoto bodyAsObject = RestHelper.getBodyAsObject(httpRequest, EIAlbumPhoto.class);
        if (null == bodyAsObject) {
            RestHelper.responseJSON(httpResponse, JsonResponse.fail(RestCode.GENERAL_ERROR, "数据不正确"));
            return;
        }

        RestHelper.oneCallAndRet(httpResponse, this.albumService, "savePhoto", bodyAsObject);
    }
}
