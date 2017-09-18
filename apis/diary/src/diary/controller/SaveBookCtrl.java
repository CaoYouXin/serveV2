package diary.controller;

import auth.AuthHelper;
import beans.BeanManager;
import diary.data.EIDiaryBook;
import diary.service.IDiaryService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;

public class SaveBookCtrl extends WithMatcher {

    private IDiaryService diaryService = BeanManager.getInstance().getService(IDiaryService.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "save a book";
    }

    @Override
    public String urlPattern() {
        return "/diary/save/book";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        EIDiaryBook eiDiaryBook = RestHelper.getBodyAsObject(httpRequest, EIDiaryBook.class);
        RestHelper.oneCallAndRet(httpResponse, diaryService, "saveBook", eiDiaryBook);
    }
}
