package blog.view;

import blog.data.EIBlogPost;
import blog.data.EIScreenshot;

import java.util.List;

public interface EIBlogPostDetail extends EIBlogPost {

    List<EIScreenshot> getScreenshots();

    void setScreenshots(List<EIScreenshot> screenshots);

}
