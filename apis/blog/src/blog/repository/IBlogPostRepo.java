package blog.repository;

import blog.data.EIBlogCategory;
import blog.data.EIBlogPost;
import blog.data.EIScreenshot;
import blog.view.EIBlogPostAndScreenshot;
import orm.Query;
import orm.Repository;

import java.util.List;

public interface IBlogPostRepo extends Repository<EIBlogPost, Long> {

    @Query(value = "select a, b from EIBlogPost a, EIScreenshot b where a.BlogPostId = b.BlogPostId and a.BlogPostDisabled = 0 and b.ScreenshotDisabled = 0", types = {EIBlogPost.class, EIScreenshot.class})
    List<EIBlogPostAndScreenshot> queryAll();

    @Query(value = "select a, b from EIBlogPost a, EIScreenshot b where a.BlogPostId = b.BlogPostId and a.BlogCategoryId = $0 and a.BlogPostDisabled = 0 and b.ScreenshotDisabled = 0", types = {EIBlogPost.class, EIScreenshot.class})
    List<EIBlogPostAndScreenshot> queryAllByCategoryId(Long categoryId);

}
