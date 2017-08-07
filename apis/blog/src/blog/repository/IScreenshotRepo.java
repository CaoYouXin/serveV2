package blog.repository;

import blog.data.EIScreenshot;
import orm.Repository;

import java.util.List;

public interface IScreenshotRepo extends Repository<EIScreenshot, Long> {

    List<EIScreenshot> findAllByBlogPostIdAndScreenshotDisabled(Long BlogPostId, Boolean disabled);

}
