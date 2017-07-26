package blog.service;

import blog.data.EIBlogCategory;
import blog.view.EIBlogCategoryNested;
import rest.Service;

import java.util.List;

public interface IBlogCategoryService extends Service {

    List<EIBlogCategory> list();

    EIBlogCategory save(EIBlogCategory blogCategory);

    List<EIBlogCategoryNested> listNestedAllUnDisabled();

}
