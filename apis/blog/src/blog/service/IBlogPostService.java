package blog.service;

import blog.data.EIBlogPost;
import blog.view.EIBlogPostDetail;
import rest.Service;

import java.util.Date;
import java.util.List;

public interface IBlogPostService extends Service {

    List<EIBlogPost> list();

    EIBlogPost save(EIBlogPost blogPost);

    List<EIBlogPostDetail> listByCategory(Long categoryId);

    List<EIBlogPostDetail> listWithDetails();

    EIBlogPost previous(Date updateTime);

    EIBlogPost next(Date updateTime);

    List<EIBlogPost> top5();

    EIBlogPost getById(Long postId);

}
