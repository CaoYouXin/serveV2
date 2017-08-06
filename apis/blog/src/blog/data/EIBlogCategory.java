package blog.data;

import beans.EntityBeanI;

import javax.persistence.*;

@Entity(name = "blog_category")
public interface EIBlogCategory extends EntityBeanI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getBlogCategoryId();

    void setBlogCategoryId(Long blogCategoryId);

    @Column(name = "pid")
    Long getParentBlogCategoryId();

    void setParentBlogCategoryId(Long parentBlogCategoryId);

    @Column(name = "name", unique = true, nullable = false)
    String getBlogCategoryName();

    void setBlogCategoryName(String blogCategoryName);

    @Column(name = "url", nullable = false, length = 1024)
    String getBlogCategoryUrl();

    void setBlogCategoryUrl(String blogCategoryUrl);

    @Column(name = "script", nullable = false, length = 1024)
    String getBlogCategoryScript();

    void setBlogCategoryScript(String blogCategoryScript);

    @Column(name = "disabled", nullable = false)
    Boolean getBlogCategoryDisabled();

    void setBlogCategoryDisabled(Boolean blogCategoryDisabled);

}
