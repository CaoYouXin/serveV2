package diary.controller;

import auth.AuthHelper;
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

public class ReleasePageCtrl extends WithMatcher {

    private IDiaryService diaryService = BeanManager.getInstance().getService(IDiaryService.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "release a page to a book";
    }

    @Override
    public String urlPattern() {
        return "/diary/release/:bookId/:pageId";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        Map<String, String> params = this.getUriPatternMatcher().getParams(httpRequest);
        String bookIdStr = params.get("bookId");
        String pageIdStr = params.get("pageId");

        try {
            Long bookId = Long.parseLong(bookIdStr);
            Long pageId = Long.parseLong(pageIdStr);

            RestHelper.oneCallAndRet(httpResponse, diaryService, "releasePage", bookId, pageId);
        } catch (Exception e) {
            RestHelper.catching(e, httpResponse, RestCode.GENERAL_ERROR);
        }
    }
}
