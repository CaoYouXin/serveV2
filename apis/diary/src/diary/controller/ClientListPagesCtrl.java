package diary.controller;

import beans.BeanManager;
import blog.auth.AuthHelperExt;
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

public class ClientListPagesCtrl extends WithMatcher {

    private IDiaryService diaryService = BeanManager.getInstance().getService(IDiaryService.class);

    @Override
    public int auth() {
        return 0;
    }

    @Override
    public String name() {
        return "client list pages";
    }

    @Override
    public String urlPattern() {
        return "/diary/client/list/page/:bookId";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        Map<String, String> params = this.getUriPatternMatcher().getParams(httpRequest);
        String bookIdStr = params.get("bookId");

        try {
            Long bookId = Long.parseLong(bookIdStr);
            RestHelper.oneCallAndRet(httpResponse, diaryService, "listSimplePages", bookId);
        } catch (Exception e) {
            RestHelper.catching(e, httpResponse, RestCode.GENERAL_ERROR);
        }
    }
}
