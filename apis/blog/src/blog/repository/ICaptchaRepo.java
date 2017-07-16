package blog.repository;

import blog.data.EICaptcha;
import orm.Query;
import orm.Repository;

import java.util.Date;

public interface ICaptchaRepo extends Repository<EICaptcha, Long> {

    @Query("select a from EICaptcha a where a.CaptchaToken = ? and a.CaptchaDeadTime > ?")
    EICaptcha queryByCaptchaTokenAndCaptchaDeadTime(String token, Date now);

}
