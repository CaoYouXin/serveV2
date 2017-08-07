package blog.repository;

import blog.data.EIBlogPost;
import blog.data.EIScreenshot;
import blog.view.EIBlogPostAndScreenshot;
import orm.Query;
import orm.Repository;

import java.util.Date;
import java.util.List;

public interface IBlogPostRepo extends Repository<EIBlogPost, Long> {

    @Query(value = "select a, b from EIBlogPost a left join EIScreenshot b on a.BlogPostId = b.BlogPostId where a.BlogPostDisabled = 0 order by b.ScreenshotId asc", types = {EIBlogPost.class, EIScreenshot.class})
    List<EIBlogPostAndScreenshot> queryAll();

    @Query(value = "select a, b from EIBlogPost a, EIScreenshot b where a.BlogPostId = b.BlogPostId and a.BlogCategoryId = $0 and a.BlogPostDisabled = 0 order by b.ScreenshotId asc", types = {EIBlogPost.class, EIScreenshot.class})
    List<EIBlogPostAndScreenshot> queryAllByCategoryId(Long categoryId);

    @Query(value = "select a from EIBlogPost a where a.BlogPostUpdateTime < $0 order by a.BlogPostUpdateTime desc limit 1")
    EIBlogPost queryPrevious(Date updateTime);

    @Query(value = "select a from EIBlogPost a where a.BlogPostUpdateTime > $0 order by a.BlogPostUpdateTime asc limit 1")
    EIBlogPost queryNext(Date updateTime);

    @Query(useValue = false)
    List<EIBlogPost> queryAllByPostIds(String sql);

    @Query(value = "select a from EIBlogPost a order by a.BlogPostUpdateTime desc limit 5")
    List<EIBlogPost> queryAllOrderByUpdateTime();

}
