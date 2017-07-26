package blog.view;

import blog.data.EIBlogCategory;

import java.util.List;

public interface EIBlogCategoryNested extends EIBlogCategory {

    List<EIBlogCategoryNested> getChildCategories();

    void setChildCategories(List<EIBlogCategoryNested> childCategories);

}
