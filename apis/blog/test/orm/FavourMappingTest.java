package orm;

import beans.BeanManager;
import blog.data.EIBlogPost;
import blog.data.EILikeRec;
import blog.data.EIResourceLevel;
import blog.data.EIScreenshot;
import blog.repository.*;
import blog.view.EIBlogPostAndScreenshot;
import blog.view.EIBlogPostDetail;
import blog.view.EIResourceLevelMappingDetail;
import blog.view.EIUserFavourDetail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

class FavourMappingTest {

    @BeforeAll
    static void beforeAll() {
        DatasourceFactory.newDataSource(
                "jdbc:mysql://localhost:3306/infinitely_serve",
                "root", "root"
        );
    }

    @Test
    void test1() {
        IResourceLevelRepo resourceLevelRepo = BeanManager.getInstance().getRepository(IResourceLevelRepo.class);
        IUserFavourMappingRepo userFavourMappingRepo = BeanManager.getInstance().getRepository(IUserFavourMappingRepo.class);

        resourceLevelRepo.createTableIfNotExist();
        userFavourMappingRepo.createTableIfNotExist();

        List<EIResourceLevel> eiResourceLevels = userFavourMappingRepo.queryByThreshold(100);
        eiResourceLevels.forEach(eiResourceLevel -> {
            System.out.println(eiResourceLevel.toJSONString());
        });
    }

    @Test
    void test2() {
        IUserFavourRepo userFavourRepo = BeanManager.getInstance().getRepository(IUserFavourRepo.class);
        IResourceLevelRepo resourceLevelRepo = BeanManager.getInstance().getRepository(IResourceLevelRepo.class);
        IUserFavourMappingRepo userFavourMappingRepo = BeanManager.getInstance().getRepository(IUserFavourMappingRepo.class);

        userFavourRepo.createTableIfNotExist();
        resourceLevelRepo.createTableIfNotExist();
        userFavourMappingRepo.createTableIfNotExist();

        List<EIResourceLevel> eiResourceLevels = resourceLevelRepo.queryByUserId(1L);
        eiResourceLevels.forEach(eiResourceLevel -> {
            System.out.println(eiResourceLevel.toJSONString());
        });
    }

    @Test
    void test3() {
        IUserFavourRepo userFavourRepo = BeanManager.getInstance().getRepository(IUserFavourRepo.class);
        IUserRepo userRepo = BeanManager.getInstance().getRepository(IUserRepo.class);

        userFavourRepo.createTableIfNotExist();
        userRepo.createTableIfNotExist();

        List<EIUserFavourDetail> eiUserFavourDetails = userFavourRepo.queryAll();
        eiUserFavourDetails.forEach(eiUserFavourDetail -> {
            System.out.println(eiUserFavourDetail.toJSONString());
        });
    }

    @Test
    void test4() {
        IResourceLevelRepo resourceLevelRepo = BeanManager.getInstance().getRepository(IResourceLevelRepo.class);
        IResourceLevelMappingRepo resourceLevelMappingRepo = BeanManager.getInstance().getRepository(IResourceLevelMappingRepo.class);

        resourceLevelRepo.createTableIfNotExist();
        resourceLevelMappingRepo.createTableIfNotExist();

        List<EIResourceLevelMappingDetail> eiResourceLevelMappingDetails = resourceLevelMappingRepo.queryAll();
        eiResourceLevelMappingDetails.forEach(eiResourceLevelMappingDetail -> {
            System.out.println(eiResourceLevelMappingDetail.toJSONString());
        });
    }

    @Test
    void test5() {
        IBlogPostRepo blogPostRepo = BeanManager.getInstance().getRepository(IBlogPostRepo.class);
        System.out.println(blogPostRepo.createTableIfNotExist());
        System.out.println(blogPostRepo.createTableIfNotExist());
    }

    @Test
    void test6() {
        IBlogPostRepo blogPostRepo = BeanManager.getInstance().getRepository(IBlogPostRepo.class);
        IScreenshotRepo screenshotRepo = BeanManager.getInstance().getRepository(IScreenshotRepo.class);

        blogPostRepo.createTableIfNotExist();
        screenshotRepo.createTableIfNotExist();

//        EIBlogPost blogPost = BeanManager.getInstance().createBean(EIBlogPost.class);
//        blogPost.setBlogCategoryId(1L);
//        blogPost.setBlogPostCreateTime(new Date());
//        blogPost.setBlogPostDisabled(false);
//        blogPost.setBlogPostName("test blog post");
//        blogPost.setBlogPostType(EIBlogPost.APP);
//        blogPost.setBlogPostScript("//a.js");
//        blogPost.setBlogPostUpdateTime(new Date());
//        blogPost.setBlogPostUrl("//a.html");
//        blogPostRepo.save(blogPost);
//
//        EIScreenshot screenshot = BeanManager.getInstance().createBean(EIScreenshot.class);
//        screenshot.setBlogPostId(blogPost.getBlogPostId());
//        screenshot.setScreenshotDisabled(false);
//        screenshot.setScreenshotUrl("//a.png");
//        screenshotRepo.save(screenshot);
//
//        EIScreenshot screenshot2 = BeanManager.getInstance().createBean(EIScreenshot.class);
//        screenshot2.setBlogPostId(blogPost.getBlogPostId());
//        screenshot2.setScreenshotDisabled(false);
//        screenshot2.setScreenshotUrl("//a2.png");
//        screenshotRepo.save(screenshot2);

        List<EIBlogPostAndScreenshot> blogPostDetails = blogPostRepo.queryAll();
        blogPostDetails.forEach(blogPostDetail -> System.out.println(blogPostDetail.toJSONString()));
    }

    @Test
    void test7() {
        IBlogPostRepo blogPostRepo = BeanManager.getInstance().getRepository(IBlogPostRepo.class);

        EIBlogPost blogPost = blogPostRepo.find(1L);

        System.out.println(blogPost.toJSONString());

        EIBlogPost next = blogPostRepo.queryNext(blogPost.getBlogPostUpdateTime());

        System.out.println(next.toJSONString());

        EIBlogPost previous = blogPostRepo.queryPrevious(next.getBlogPostUpdateTime());

        System.out.println(previous.toJSONString());
    }

    @Test
    void test8() {
        ILikeRecRepo likeRecRepo = BeanManager.getInstance().getRepository(ILikeRecRepo.class);

        likeRecRepo.createTableIfNotExist();

        List<EILikeRec> likeRecs = likeRecRepo.queryTop5();

        likeRecs.forEach(likeRec -> System.out.println(likeRec.toJSONString()));
    }

}
