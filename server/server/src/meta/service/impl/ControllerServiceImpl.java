package meta.service.impl;

import beans.BeanManager;
import config.Configs;
import meta.data.EIController;
import meta.repository.IControllerRepo;
import meta.service.IControllerService;
import meta.service.exp.ControllerSetException;
import rest.Controller;
import util.loader.CustomClassLoader;

import java.util.List;

public class ControllerServiceImpl implements IControllerService {

    private IControllerRepo controllerRepo = BeanManager.getInstance().getRepository(IControllerRepo.class);

    @Override
    public List<EIController> listControllers() throws ControllerSetException {
        if (!this.controllerRepo.createTableIfNotExist()) {
            throw new ControllerSetException("controller 表无法创建.");
        }

        return this.controllerRepo.findAll();
    }

    @Override
    public EIController setController(String className) throws ControllerSetException {
        if (!this.controllerRepo.createTableIfNotExist()) {
            throw new ControllerSetException("controller 表无法创建.");
        }

        CustomClassLoader classLoader = Configs.getConfigs(Configs.CLASSLOADER, CustomClassLoader.class);
        String name = BeanManager.getInstance().setController((Class<? extends Controller>) classLoader.loadClass(className));

        if (null == name) {
            throw new ControllerSetException("class can not be initialed.");
        }

        EIController byControllerName = this.controllerRepo.findByControllerName(name);
        if (null == byControllerName) {
            byControllerName = BeanManager.getInstance().createBean(EIController.class);
            byControllerName.setControllerName(name);
            byControllerName.setDisabled(false);
        }
        byControllerName.setControllerClassName(className);

        if (!this.controllerRepo.save(byControllerName)) {
            throw new ControllerSetException("controller def can to be saved.");
        }

        return byControllerName;
    }

    @Override
    public Boolean setControllerDisabled(Long id, Boolean disabled) throws ControllerSetException {
        if (!this.controllerRepo.createTableIfNotExist()) {
            throw new ControllerSetException("controller 表无法创建.");
        }

        EIController controller = this.controllerRepo.find(id);
        if (null == controller) {
            throw new ControllerSetException("controller 【" + id + "】can not be found.");
        }

        controller.setDisabled(disabled);

        if (!this.controllerRepo.save(controller)) {
            throw new ControllerSetException("controller def can to be saved.");
        }

        return disabled;
    }
}
