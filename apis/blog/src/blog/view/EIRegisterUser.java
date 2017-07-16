package blog.view;

import blog.data.EIUser;

public interface EIRegisterUser extends EIUser {

    String getImageCaptcha();

    void setImageCaptcha(String imageCaptcha);

    String getImageCaptchaToken();

    void setImageCaptchaToken(String imageCaptchaToken);

}
