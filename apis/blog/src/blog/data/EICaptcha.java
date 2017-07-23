package blog.data;

import beans.EntityBeanI;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "blog_image_captcha")
public interface EICaptcha extends EntityBeanI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getCaptchaId();

    void setCaptchaId(Long captchaId);

    @Column(name = "code", nullable = false)
    String getCaptchaCode();

    void setCaptchaCode(String captchaCode);

    @Column(name = "token", nullable = false, unique = true)
    String getCaptchaToken();

    void setCaptchaToken(String captchaToken);

    @Column(name = "dead_time", nullable = false)
    Date getCaptchaDeadTime();

    void setCaptchaDeadTime(Date captchaDeadTime);

}
