package blog.controller;

import beans.BeanManager;
import blog.service.ICaptchaService;
import blog.service.exp.CaptchaException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.HelperController;
import rest.RestCode;
import rest.RestHelper;

import java.io.IOException;
import java.util.Map;

public class UserRegisterCaptchaCtrl extends HelperController {

    private ICaptchaService captchaService = BeanManager.getInstance().getService(ICaptchaService.class);

    @Override
    public String name() {
        return "blog user register captcha";
    }

    @Override
    public String urlPattern() {
        return "/blog/user/register/captcha/:width/:height?token=:token";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        Map<String, String> params = this.getUriPatternMatcher().getParams(httpRequest);
        String width = params.get("width");
        String height = params.get("height");
        String token = params.get("token");

        byte[] bytes;
        try {
            bytes = this.captchaService.generateCaptcha(token, Integer.parseInt(width), Integer.parseInt(height));
        } catch (CaptchaException e) {
            RestHelper.catching(e, httpResponse, RestCode.GENERAL_ERROR);
            return;
        }
        RestHelper.responseImage(httpResponse, bytes, "image/jpg");
    }
}
