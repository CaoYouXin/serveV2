package diary.controller;

import auth.AuthHelper;
import beans.BeanManager;
import diary.data.EIDiaryMilestone;
import diary.data.EIDiaryPage;
import diary.service.IDiaryService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;

public class SaveMilestoneCtrl extends WithMatcher {

    private IDiaryService diaryService = BeanManager.getInstance().getService(IDiaryService.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "save a milestone";
    }

    @Override
    public String urlPattern() {
        return "/diary/save/milestone";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        EIDiaryMilestone eiDiaryMilestone = RestHelper.getBodyAsObject(httpRequest, EIDiaryMilestone.class);
        RestHelper.oneCallAndRet(httpResponse, diaryService, "saveMilestone", eiDiaryMilestone);
    }
}
