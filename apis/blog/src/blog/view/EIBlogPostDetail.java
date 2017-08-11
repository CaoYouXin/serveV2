package blog.view;

import blog.data.EIBlogCategory;
import blog.data.EIBlogPost;
import blog.data.EIScreenshot;

import java.util.List;

public interface EIBlogPostDetail extends EIBlogPost, EIBlogCategory {

    List<EIScreenshot> getScreenshots();

    void setScreenshots(List<EIScreenshot> screenshots);

}
