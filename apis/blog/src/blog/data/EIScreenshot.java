package blog.data;

import beans.EntityBeanI;

import javax.persistence.*;

@Entity(name = "blog_screenshot")
public interface EIScreenshot extends EntityBeanI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getScreenshotId();

    void setScreenshotId(Long screenshotId);

    @Column(name = "post_id", nullable = false)
    Long getBlogPostId();

    void setBlogPostId(Long blogPostId);

    @Column(name = "url", length = 1024, nullable = false)
    String getScreenshotUrl();

    void setScreenshotUrl(String screenshotUrl);

    @Column(name = "disabled", nullable = false)
    Boolean getScreenshotDisabled();

    void setScreenshotDisabled(Boolean screenshotDisabled);

}
