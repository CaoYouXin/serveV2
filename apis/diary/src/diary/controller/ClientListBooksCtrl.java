package diary.controller;

import auth.AuthHelper;
import beans.BeanManager;
import blog.auth.AuthHelperExt;
import blog.auth.BlogLoginAuth;
import diary.service.IDiaryService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;

public class ClientListBooksCtrl extends WithMatcher {

    private IDiaryService diaryService = BeanManager.getInstance().getService(IDiaryService.class);

    @Override
    public int auth() {
        return AuthHelperExt.BLOG_LOGIN | AuthHelper.ALWAYS_TRUE;
    }

    @Override
    public String name() {
        return "client list books";
    }

    @Override
    public String urlPattern() {
        return "/diary/client/list/book";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        Object userId = httpContext.getAttribute(BlogLoginAuth.USER_ID_KEY);
        RestHelper.oneCallAndRet(httpResponse, diaryService, "listBooks", userId);
    }
}
