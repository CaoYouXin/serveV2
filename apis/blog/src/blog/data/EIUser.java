package blog.data;

import beans.EntityBeanI;

import javax.persistence.*;

@Entity(name = "blog_user")
public interface EIUser extends EntityBeanI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getUserId();

    void setUserId(Long userId);

    @Column(name = "username", nullable = false, unique = true)
    String getUserName();

    void setUserName(String userName);

    @Column(name = "password", nullable = false)
    String getUserPassword();

    void setUserPassword(String pwd);

    @Column(name = "nickname")
    String getUserNickName();

    void setUserNickName(String nickName);

    @Column(name = "sex")
    Byte getUserSex();

    void setUserSex(Byte sex);

    @Column(name = "age")
    Short getUserAge();

    void setUserAge(Short age);

    @Column(name = "profession")
    String getUserProfession();

    void setUserProfession(String profession);

    @Column(name = "avatar", length = 1024)
    String getUserAvatar();

    void setUserAvatar(String avatar);
}
