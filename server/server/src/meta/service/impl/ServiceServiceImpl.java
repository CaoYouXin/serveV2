package meta.service.impl;

import beans.BeanManager;
import config.Configs;
import meta.data.EIService;
import meta.repository.IServiceRepo;
import meta.service.IServiceService;
import meta.service.exp.ServiceSetException;
import rest.Service;
import util.loader.CustomClassLoader;

import java.util.List;

public class ServiceServiceImpl implements IServiceService {

    private IServiceRepo serviceRepo = BeanManager.getInstance().getRepository(IServiceRepo.class);

    @Override
    public void initAllService() throws ServiceSetException {
        if (!this.serviceRepo.createTableIfNotExist()) {
            throw new ServiceSetException("无法创建 service 表。");
        }

        List<EIService> all = this.serviceRepo.findAllByServiceDisabled(false);
        all.forEach(this::setupService);
    }

    @Override
    public List<EIService> listServices() throws ServiceSetException {
        if (!this.serviceRepo.createTableIfNotExist()) {
            throw new ServiceSetException("无法创建 service 表。");
        }

        return this.serviceRepo.findAll();
    }

    @Override
    public EIService setService(String intfClassName, String implClassName) throws ServiceSetException {
        if (!this.serviceRepo.createTableIfNotExist()) {
            throw new ServiceSetException("无法创建 service 表。");
        }

        EIService eiService = this.serviceRepo.findByServiceIntfClassName(intfClassName);
        if (null == eiService) {
            eiService = BeanManager.getInstance().createBean(EIService.class);
            eiService.setServiceIntfClassName(intfClassName);
            eiService.setServiceDisabled(false);
        }
        eiService.setServiceImplClassName(implClassName);

        if (!this.serviceRepo.save(eiService)) {
            throw new ServiceSetException("无法保存 service 。");
        }

        this.setupService(eiService);

        return eiService;
    }

    @Override
    public Boolean disableService(Long id, Boolean disabled) throws ServiceSetException {
        if (!this.serviceRepo.createTableIfNotExist()) {
            throw new ServiceSetException("无法创建 service 表。");
        }

        EIService eiService = this.serviceRepo.find(id);
        if (null == eiService) {
            throw new ServiceSetException(id + " service 不存在。");
        }

        eiService.setServiceDisabled(disabled);

        if (!this.serviceRepo.save(eiService)) {
            throw new ServiceSetException("无法保存 service 。");
        }

        this.setupService(eiService);

        return eiService.isServiceDisabled();
    }

    private void setupService(EIService eiService) {
        if (eiService.isServiceDisabled()) {
            return;
        }

        CustomClassLoader classLoader = Configs.getConfigs(Configs.CLASSLOADER, CustomClassLoader.class);

        BeanManager.getInstance().setService(
                (Class<Service>) classLoader.loadClass(eiService.getServiceIntfClassName()),
                (Class<? extends Service>) classLoader.loadClass(eiService.getServiceImplClassName())
        );
    }
}
