package blog.service;

import blog.data.EIBlogPost;
import blog.view.EIBlogPostDetail;
import rest.Service;

import java.util.List;

public interface IBlogPostService extends Service {

    List<EIBlogPost> list();

    EIBlogPost save(EIBlogPost blogPost);

    List<EIBlogPostDetail> listByCategory(Long categoryId);

    List<EIBlogPostDetail> listWithDetails();

}
