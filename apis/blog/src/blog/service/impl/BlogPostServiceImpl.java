package blog.service.impl;

import beans.BeanManager;
import blog.data.EIBlogPost;
import blog.data.EILikeRec;
import blog.data.EIScreenshot;
import blog.repository.IBlogPostRepo;
import blog.repository.ILikeRecRepo;
import blog.service.IBlogPostService;
import blog.service.base.BaseService;
import blog.view.EIBlogPostAndScreenshot;
import blog.view.EIBlogPostDetail;
import orm.Repository;

import java.util.*;
import java.util.stream.Collectors;

public class BlogPostServiceImpl extends BaseService<EIBlogPost, Long> implements IBlogPostService {

    private IBlogPostRepo blogPostRepo = BeanManager.getInstance().getRepository(IBlogPostRepo.class);
    private ILikeRecRepo likeRecRepo = BeanManager.getInstance().getRepository(ILikeRecRepo.class);

    @Override
    public List<EIBlogPostDetail> listByCategory(Long categoryId) {
        return this.transform(this.blogPostRepo.queryAllByCategoryId(categoryId));
    }

    @Override
    public List<EIBlogPostDetail> listWithDetails() {
        return this.transform(this.blogPostRepo.queryAll());
    }

    @Override
    public EIBlogPost previous(Date updateTime) {
        return this.blogPostRepo.queryPrevious(updateTime);
    }

    @Override
    public EIBlogPost next(Date updateTime) {
        return this.blogPostRepo.queryNext(updateTime);
    }

    @Override
    public List<EIBlogPost> top5() {
        List<Long> ids = new ArrayList<>();

        List<EILikeRec> likeRecs = this.likeRecRepo.queryTop5();
        likeRecs.forEach(likeRec -> ids.add(likeRec.getBlogPostId()));

        if (likeRecs.size() < 5) {
            List<EIBlogPost> blogPosts = this.blogPostRepo.queryAllOrderByUpdateTime();

            for (int i = 0; i < 5 - likeRecs.size(); i++) {
                ids.add(blogPosts.get(i).getBlogPostId());
            }
        }

        StringJoiner inClause = new StringJoiner(",", "(", ")");
        ids.forEach(id -> inClause.add(Long.toString(id)));
        String sql = String.format("select a from EIBlogPost a where a.BlogCategoryId in %s", inClause.toString());

        return this.blogPostRepo.queryAllByPostIds(sql);
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
