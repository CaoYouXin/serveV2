package blog.controller;

import beans.BeanManager;
import blog.service.IBlogPostService;
import config.Configs;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.JsonResponse;
import rest.RestCode;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public class GetPreviousPostCtrl extends WithMatcher {

    private IBlogPostService blogPostService = BeanManager.getInstance().getService(IBlogPostService.class);

    @Override
    public String name() {
        return "blog get previous post";
    }

    @Override
    public String urlPattern() {
        return "/blog/previous-post/:date";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        Map<String, String> params = this.getUriPatternMatcher().getParams(httpRequest);
        DateFormat dateFormat = Configs.getConfigs(Configs.DATE_FORMAT, DateFormat.class);
        Date date = null;
        try {
            date = dateFormat.parse(params.get("date"));
        } catch (ParseException e) {
            RestHelper.responseJSON(httpResponse, JsonResponse.fail(RestCode.GENERAL_ERROR, e.getMessage()));
            return;
        }

        RestHelper.oneCallAndRet(httpResponse, this.blogPostService, "previous", date);
    }
}
