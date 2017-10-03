package album.controller;

import album.data.EIAlbumAlbum;
import album.service.IAlbumService;
import beans.BeanManager;
import blog.auth.AuthHelperExt;
import blog.auth.BlogLoginAuth;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.JsonResponse;
import rest.RestCode;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;

public class SaveAlbumCtrl extends WithMatcher {

    private IAlbumService albumService = BeanManager.getInstance().getService(IAlbumService.class);

    @Override
    public int auth() {
        return AuthHelperExt.BLOG_LOGIN;
    }

    @Override
    public String name() {
        return "album save a album";
    }

    @Override
    public String urlPattern() {
        return "/album/album/save";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        if (!RestHelper.isPost(httpRequest, httpResponse)) {
            return;
        }

        Long userId = (Long) httpContext.getAttribute(BlogLoginAuth.USER_ID_KEY);

        if (null == userId) {
            RestHelper.responseJSON(httpResponse, JsonResponse.fail(RestCode.GENERAL_ERROR, "无法获取用户ID"));
            return;
        }

        EIAlbumAlbum bodyAsObject = RestHelper.getBodyAsObject(httpRequest, EIAlbumAlbum.class);
        if (null == bodyAsObject) {
            RestHelper.responseJSON(httpResponse, JsonResponse.fail(RestCode.GENERAL_ERROR, "数据不正确"));
            return;
        }

        bodyAsObject.setUserId(userId);
        RestHelper.oneCallAndRet(httpResponse, this.albumService, "saveAlbum", bodyAsObject);
    }
}
