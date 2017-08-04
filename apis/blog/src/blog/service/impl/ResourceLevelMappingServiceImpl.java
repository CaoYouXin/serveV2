package blog.service.impl;

import beans.BeanManager;
import blog.auth.ResourceLevelServeAuth;
import blog.data.EIResourceLevelMapping;
import blog.repository.IResourceLevelMappingRepo;
import blog.repository.IResourceLevelRepo;
import blog.service.IResourceLevelMappingService;
import blog.service.base.BaseService;
import blog.service.exp.TableNotCreateException;
import blog.view.EIResourceLevelMappingDetail;
import config.Configs;
import orm.Repository;

import java.util.List;

public class ResourceLevelMappingServiceImpl extends BaseService<EIResourceLevelMapping, Long> implements IResourceLevelMappingService {

    private IResourceLevelMappingRepo resourceLevelMappingRepo = BeanManager.getInstance().getRepository(IResourceLevelMappingRepo.class);
    private IResourceLevelRepo resourceLevelRepo = BeanManager.getInstance().getRepository(IResourceLevelRepo.class);

    @Override
    protected Repository<EIResourceLevelMapping, Long> getRepository() {
        return this.resourceLevelMappingRepo;
    }

    @Override
    protected String getName() {
        return "resource level mapping";
    }

    @Override
    public void before() {
        super.before();

        if (!this.resourceLevelRepo.createTableIfNotExist()) {
            throw new TableNotCreateException("resource level");
        }
    }

    @Override
    public List<EIResourceLevelMapping> list() {
        return this.resourceLevelMappingRepo.findAllByResourceLevelMappingDisabled(false);
    }

    @Override
    public EIResourceLevelMapping save(EIResourceLevelMapping data) {
        if (null == data.getResourceLevelMappingDisabled()) {
            data.setResourceLevelMappingDisabled(false);
        }
        EIResourceLevelMapping save = super.save(data);
        Configs.setConfigs(ResourceLevelServeAuth.RESOURCE_LEVEL_SERVE_AUTH_CONFIG_KEY, false);
        return save;
    }

    @Override
    public List<EIResourceLevelMappingDetail> listDetails() {
        return this.resourceLevelMappingRepo.queryAll();
    }
}
