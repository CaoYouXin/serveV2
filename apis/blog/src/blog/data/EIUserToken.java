package blog.data;

import beans.EntityBeanI;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "blog_user_token")
public interface EIUserToken extends EntityBeanI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getUserTokenId();

    void setUserTokenId(Long userTokenId);

    @Column(name = "user_id")
    Long getUserId();

    void setUserId(Long userId);

    @Column(name = "token", unique = true)
    String getUserToken();

    void setUserToken(String token);

    @Column(name = "dead_time")
    Date getUserTokenDeadTime();

    void setUserTokenDeadTime(Date userTokenDeadTime);

}
