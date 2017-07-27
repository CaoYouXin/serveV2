package blog.service.impl;

import beans.BeanManager;
import blog.data.EIBlogPost;
import blog.data.EIScreenshot;
import blog.repository.IBlogPostRepo;
import blog.service.IBlogPostService;
import blog.service.base.BaseService;
import blog.view.EIBlogPostAndScreenshot;
import blog.view.EIBlogPostDetail;
import orm.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BlogPostServiceImpl extends BaseService<EIBlogPost, Long> implements IBlogPostService {

    private IBlogPostRepo blogPostRepo = BeanManager.getInstance().getRepository(IBlogPostRepo.class);

    @Override
    public List<EIBlogPostDetail> listByCategory(Long categoryId) {
        return this.transform(this.blogPostRepo.queryAllByCategoryId(categoryId));
    }

    @Override
    public List<EIBlogPostDetail> listWithDetails() {
        return this.transform(this.blogPostRepo.queryAll());
    }

    private List<EIBlogPostDetail> transform(List<EIBlogPostAndScreenshot> data) {
        Map<Long, EIBlogPostDetail> ret = new HashMap<>();

        for (EIBlogPostAndScreenshot blogPostAndScreenshot : data) {
            EIBlogPostDetail blogPostDetail = ret.get(blogPostAndScreenshot.getBlogPostId());
            if (null == blogPostDetail) {
                blogPostDetail = BeanManager.getInstance().createBean(EIBlogPostDetail.class);
                blogPostDetail.copyFrom(blogPostAndScreenshot, EIBlogPost.class);
                blogPostDetail.setScreenshots(new ArrayList<>());

                ret.put(blogPostAndScreenshot.getBlogPostId(), blogPostDetail);
            }

            EIScreenshot screenshot = BeanManager.getInstance().createBean(EIScreenshot.class);
            screenshot.copyFrom(blogPostAndScreenshot, EIScreenshot.class);

            blogPostDetail.getScreenshots().add(screenshot);
        }

        return new ArrayList<>(ret.values());
    }

    @Override
    protected Repository<EIBlogPost, Long> getRepository() {
        return this.blogPostRepo;
    }

    @Override
    protected String getName() {
        return "blog post";
    }
}
