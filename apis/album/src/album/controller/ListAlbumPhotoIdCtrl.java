package album.controller;

import album.service.IAlbumService;
import beans.BeanManager;
import blog.auth.AuthHelperExt;
import blog.auth.BlogLoginAuth;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.*;

import java.io.IOException;

public class ListAlbumPhotoIdCtrl extends WithMatcher {

    private IAlbumService albumService = BeanManager.getInstance().getService(IAlbumService.class);

    @Override
    public int auth() {
        return AuthHelperExt.BLOG_LOGIN;
    }

    @Override
    public String name() {
        return "list album photo ids";
    }

    @Override
    public String urlPattern() {
        return "/album/:albumId/photo/list/ids";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        Long userId = (Long) httpContext.getAttribute(BlogLoginAuth.USER_ID_KEY);

        if (null == userId) {
            RestHelper.responseJSON(httpResponse, JsonResponse.fail(RestCode.GENERAL_ERROR, "无法获取用户ID"));
            return;
        }

        ParamsHelper paramsHelper = this.getUriPatternMatcher().getParamsHelper(httpRequest);

        try {
            Long albumId = paramsHelper.getLong("albumId");

            RestHelper.oneCallAndRet(httpResponse, this.albumService, "listAlbumPhotoIds", userId, albumId);
        } catch (Exception e) {

            RestHelper.catching(e, httpResponse, RestCode.GENERAL_ERROR);
        }
    }
}
