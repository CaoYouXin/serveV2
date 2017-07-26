package blog.repository;

import blog.data.EIBlogCategory;
import orm.Repository;

import java.util.List;

public interface IBlogCategoryRepo extends Repository<EIBlogCategory, Long> {

    List<EIBlogCategory> findAllByBlogCategoryDisabled(Boolean disabled);

}
