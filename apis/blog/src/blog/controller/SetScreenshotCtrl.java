package blog.controller;

import auth.AuthHelper;
import beans.BeanManager;
import blog.data.EIScreenshot;
import blog.service.IScreenshotService;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;

public class SetScreenshotCtrl extends WithMatcher {

    private IScreenshotService screenshotService = BeanManager.getInstance().getService(IScreenshotService.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "blog set screenshot";
    }

    @Override
    public String urlPattern() {
        return "/blog/screenshot/set";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        if (!RestHelper.isPost(httpRequest, httpResponse)) {
            return;
        }

        EIScreenshot screenshot = RestHelper.getBodyAsObject(httpRequest, EIScreenshot.class);
        RestHelper.oneCallAndRet(httpResponse, this.screenshotService, "save", screenshot);
    }
}
