package blog.service;

import blog.service.exp.CaptchaException;
import rest.Service;

public interface ICaptchaService extends Service {

    byte[] generateCaptcha(String token, int width, int height) throws CaptchaException;

}
