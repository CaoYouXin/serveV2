package meta.service.impl;

import beans.BeanManager;
import config.Configs;
import meta.data.EIController;
import meta.repository.IControllerRepo;
import meta.service.IControllerService;
import meta.service.exp.ControllerSetException;
import rest.Controller;
import rest.UriPatternMatcher;
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
            byControllerName.setControllerDisabled(false);
        }
        byControllerName.setControllerClassName(className);

        if (!this.controllerRepo.save(byControllerName)) {
            throw new ControllerSetException("controller def can to be saved.");
        }

        this.setupApi(byControllerName);

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

        controller.setControllerDisabled(disabled);

        if (!this.controllerRepo.save(controller)) {
            throw new ControllerSetException("controller def can to be saved.");
        }

        this.setupApi(controller);

        return disabled;
    }

    @Override
    public void initAllControllers() throws ControllerSetException {
        if (!this.controllerRepo.createTableIfNotExist()) {
            throw new ControllerSetException("controller 表无法创建.");
        }

        List<EIController> all = this.controllerRepo.findAllByControllerDisabled(false);
        for (EIController eiController : all) {
            CustomClassLoader classLoader = Configs.getConfigs(Configs.CLASSLOADER, CustomClassLoader.class);
            String name = BeanManager.getInstance().setController(
                    (Class<? extends Controller>) classLoader.loadClass(eiController.getControllerClassName())
            );

            if (!name.equals(eiController.getControllerName())) {
                throw new ControllerSetException("class did not initiate correctly.");
            }

            this.setupApi(eiController);
        }
    }

    private void setupApi(EIController controller) {
        List<Controller> apis = Configs.getConfigs(Configs.APIS, List.class);

        if (controller.isControllerDisabled()) {
            EIController finalByControllerName = controller;
            apis.removeIf(api -> api.name().equals(finalByControllerName.getControllerName()));

        } else if (!controller.isControllerDisabled()) {
            for (Controller api : apis) {
                if (api.name().equals(controller.getControllerName())) {
                    this.setMatcher(api);
                    return;
                }
            }

            Controller realController = BeanManager.getInstance().getController(controller.getControllerName());
            apis.add(this.setMatcher(realController));
        }
    }

    private Controller setMatcher(Controller controller) {
        UriPatternMatcher uriPatternMatcher = new UriPatternMatcher("/api");
        uriPatternMatcher.setController(controller);
        controller.setUriPatternMatcher(uriPatternMatcher);
        return controller;
    }
}
