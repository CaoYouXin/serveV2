package album.controller;

import album.service.IAlbumService;
import beans.BeanManager;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;

public class ListAllPublicVideoCtrl extends WithMatcher {

    private IAlbumService albumService = BeanManager.getInstance().getService(IAlbumService.class);

    @Override
    public String name() {
        return "album list all public videos";
    }

    @Override
    public String urlPattern() {
        return "/album/video/list";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        RestHelper.oneCallAndRet(httpResponse, this.albumService, "listPublicAlbumVideo");
    }
}
