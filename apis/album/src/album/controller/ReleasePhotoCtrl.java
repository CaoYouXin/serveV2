package album.controller;

import album.service.IAlbumService;
import beans.BeanManager;
import blog.auth.AuthHelperExt;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.ParamsHelper;
import rest.RestCode;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;

public class ReleasePhotoCtrl extends WithMatcher {

    private IAlbumService albumService = BeanManager.getInstance().getService(IAlbumService.class);

    @Override
    public int auth() {
        return AuthHelperExt.BLOG_LOGIN;
    }

    @Override
    public String name() {
        return "album release photo";
    }

    @Override
    public String urlPattern() {
        return "/album/release/:albumId/:photoId";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        ParamsHelper paramsHelper = this.getUriPatternMatcher().getParamsHelper(httpRequest);

        try {
            Long albumId = paramsHelper.getLong("albumId");
            Long photoId = paramsHelper.getLong("photoId");

            RestHelper.oneCallAndRet(httpResponse, this.albumService, "releasePhoto", albumId, photoId);
        } catch (Exception e) {

            RestHelper.catching(e, httpResponse, RestCode.GENERAL_ERROR);
        }
    }
}
