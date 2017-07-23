package blog.service.impl;

import beans.BeanManager;
import blog.data.EIResourceLevel;
import blog.repository.IResourceLevelRepo;
import blog.service.IResourceLevelService;
import blog.service.base.BaseService;
import orm.Repository;

public class ResourceLevelServiceImpl extends BaseService<EIResourceLevel, Long> implements IResourceLevelService {

    private IResourceLevelRepo resourceLevelRepo = BeanManager.getInstance().getRepository(IResourceLevelRepo.class);

    @Override
    protected Repository<EIResourceLevel, Long> getRepository() {
        return this.resourceLevelRepo;
    }

    @Override
    protected String getName() {
        return "resource level";
    }
}
