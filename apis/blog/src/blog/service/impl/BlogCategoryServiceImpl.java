package blog.service.impl;

import beans.BeanManager;
import blog.data.EIBlogCategory;
import blog.repository.IBlogCategoryRepo;
import blog.service.IBlogCategoryService;
import blog.service.base.BaseService;
import blog.view.EIBlogCategoryNested;
import orm.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlogCategoryServiceImpl extends BaseService<EIBlogCategory, Long> implements IBlogCategoryService {

    private IBlogCategoryRepo blogCategoryRepo = BeanManager.getInstance().getRepository(IBlogCategoryRepo.class);

    @Override
    public List<EIBlogCategoryNested> listNestedAllUnDisabled() {
        List<EIBlogCategoryNested> ret = new ArrayList<>();

        List<EIBlogCategory> allByBlogCategoryDisabled = this.blogCategoryRepo.findAllByBlogCategoryDisabled(false);

        if (allByBlogCategoryDisabled.isEmpty()) {
            return ret;
        }

        int size = allByBlogCategoryDisabled.size();
        Map<Long, EIBlogCategoryNested> searchPIDs = new HashMap<>();
        while (size > 0) {
            for (EIBlogCategory eiBlogCategory : allByBlogCategoryDisabled) {
                if (this.canProcess(searchPIDs, eiBlogCategory)) {
                    EIBlogCategoryNested blogCategoryNested = BeanManager.getInstance().createBean(EIBlogCategoryNested.class);
                    blogCategoryNested.copyFrom(eiBlogCategory);

                    if (this.isTopCategory(eiBlogCategory)) {
                        ret.add(blogCategoryNested);
                    } else {
                        EIBlogCategoryNested eiBlogCategoryNested = searchPIDs.get(eiBlogCategory.getParentBlogCategoryId());
                        if (null == eiBlogCategoryNested.getChildCategories()) {
                            eiBlogCategoryNested.setChildCategories(new ArrayList<>());
                        }

                        eiBlogCategoryNested.getChildCategories().add(blogCategoryNested);
                    }

                    searchPIDs.put(eiBlogCategory.getBlogCategoryId(), blogCategoryNested);
                    size--;
                }
            }
        }

        return ret;
    }

    private boolean canProcess(Map<Long, EIBlogCategoryNested> searchPIDs, EIBlogCategory eiBlogCategory) {
        return !this.isCategoryLinked(searchPIDs, eiBlogCategory)
                && (this.isTopCategory(eiBlogCategory)
                || this.isParentCategoryLinked(searchPIDs, eiBlogCategory));
    }

    private boolean isCategoryLinked(Map<Long, EIBlogCategoryNested> searchPIDs, EIBlogCategory blogCategory) {
        return searchPIDs.containsKey(blogCategory.getBlogCategoryId());
    }

    private boolean isParentCategoryLinked(Map<Long, EIBlogCategoryNested> searchPIDs, EIBlogCategory blogCategory) {
        return searchPIDs.containsKey(blogCategory.getParentBlogCategoryId());
    }

    private boolean isTopCategory(EIBlogCategory eiBlogCategory) {
        return null == eiBlogCategory.getParentBlogCategoryId();
    }

    @Override
    protected Repository<EIBlogCategory, Long> getRepository() {
        return this.blogCategoryRepo;
    }

    @Override
    protected String getName() {
        return "blog category";
    }
}
