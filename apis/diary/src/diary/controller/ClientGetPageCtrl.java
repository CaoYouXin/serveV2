package diary.controller;

import beans.BeanManager;
import diary.service.IDiaryService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.RestCode;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;
import java.util.Map;

public class ClientGetPageCtrl extends WithMatcher {

    private IDiaryService diaryService = BeanManager.getInstance().getService(IDiaryService.class);

    @Override
    public int auth() {
        return 0;
    }

    @Override
    public String name() {
        return "client get a page";
    }

    @Override
    public String urlPattern() {
        return "/diary/client/get/page/:pageId";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        Map<String, String> params = this.getUriPatternMatcher().getParams(httpRequest);
        String pageIdStr = params.get("pageId");

        try {
            Long pageId = Long.parseLong(pageIdStr);
            RestHelper.oneCallAndRet(httpResponse, diaryService, "getDetailedPage", pageId);
        } catch (Exception e) {
            RestHelper.catching(e, httpResponse, RestCode.GENERAL_ERROR);
        }
    }
}
