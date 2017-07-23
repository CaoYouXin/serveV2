package blog.data;

import beans.EntityBeanI;

import javax.persistence.*;

@Entity(name = "blog_user_favour")
public interface EIUserFavour extends EntityBeanI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getUserFavourId();

    void setUserFavourId(Long userFavourId);

    @Column(name = "user_id")
    Long getUserId();

    void setUserId(Long userId);

    @Column(name = "favour_value")
    Integer getUserFavourValue();

    void setUserFavourValue(Integer userFavourValue);

}
