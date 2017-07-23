package blog.service.impl;

import beans.BeanManager;
import blog.auth.ResourceLevelServeAuth;
import blog.data.EIResourceLevelMapping;
import blog.repository.IResourceLevelMappingRepo;
import blog.service.IResourceLevelMappingService;
import blog.service.base.BaseService;
import config.Configs;
import orm.Repository;

import java.util.List;

public class ResourceLevelMappingServiceImpl extends BaseService<EIResourceLevelMapping, Long> implements IResourceLevelMappingService {

    private IResourceLevelMappingRepo resourceLevelMappingRepo = BeanManager.getInstance().getRepository(IResourceLevelMappingRepo.class);

    @Override
    protected Repository<EIResourceLevelMapping, Long> getRepository() {
        return this.resourceLevelMappingRepo;
    }

    @Override
    protected String getName() {
        return "resource level mapping";
    }

    @Override
    public Boolean delete(Long id) {
        return this.resourceLevelMappingRepo.softRemoveByResourceLevelMappingIdAtResourceLevelMappingDisabled(id);
    }

    @Override
    public List<EIResourceLevelMapping> list() {
        return this.resourceLevelMappingRepo.findAllByResourceLevelMappingDisabled(false);
    }

    @Override
    public EIResourceLevelMapping save(EIResourceLevelMapping data) {
        Configs.setConfigs(ResourceLevelServeAuth.RESOURCE_LEVEL_SERVE_AUTH_CONFIG_KEY, false);
        return super.save(data);
    }
}
