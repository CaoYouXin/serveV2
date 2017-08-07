package blog.data;

import beans.EntityBeanI;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "blog_post")
public interface EIBlogPost extends EntityBeanI {

    Byte APP = 1;
    Byte ARTICLE = 2;

    String STR_APP = "APP";
    String STR_ARTICLE = "文章";

    default String toString(Byte type) {
        switch (type) {
            case 1:
                return STR_APP;
            case 2:
                return STR_ARTICLE;
            default:
                return "未知类型";
        }
    }

    default Byte parse(String type) {
        switch (type) {
            case STR_ARTICLE:
                return ARTICLE;
            case STR_APP:
            default:
                return APP;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getBlogPostId();

    void setBlogPostId(Long blogPostId);

    @Column(name = "category_id", nullable = false)
    Long getBlogCategoryId();

    void setBlogCategoryId(Long blogCategoryId);

    @Column(name = "name", unique = true, nullable = false)
    String getBlogPostName();

    void setBlogPostName(String blogPostName);

    @Column(name = "type", nullable = false)
    Byte getBlogPostType();

    void setBlogPostType(Byte blogPostType);

    @Column(name = "url", nullable = false, length = 1024)
    String getBlogPostUrl();

    void setBlogPostUrl(String blogPostUrl);

    @Column(name = "script", nullable = false, length = 1024)
    String getBlogPostScript();

    void setBlogPostScript(String blogPostScript);

    @Column(name = "create", nullable = false)
    Date getBlogPostCreateTime();

    void setBlogPostCreateTime(Date blogPostCreateTime);

    @Column(name = "update", nullable = false)
    Date getBlogPostUpdateTime();

    void setBlogPostUpdateTime(Date blogPostUpdateTime);

    @Column(name = "disabled", nullable = false)
    Boolean getBlogPostDisabled();

    void setBlogPostDisabled(Boolean blogPostDisabled);

}
