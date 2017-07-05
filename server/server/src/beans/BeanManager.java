package beans;

import config.Configs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rest.Controller;
import rest.Service;
import util.loader.CustomClassLoader;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class BeanManager {

    private static final Logger logger = LogManager.getLogger(BeanManager.class);

    private static final BeanManager INSTANCE = new BeanManager();

    public static BeanManager getInstance() {
        return INSTANCE;
    }

    private BeanManager() {}

    private Map<String, Service> services = new HashMap<>();

    public <T extends Service, Impl extends T> void setService(Class<T> serviceClass, Class<Impl> serviceImplClass) {
        T t = null;
        try {
            t = serviceImplClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.catching(e);
            return;
        }

        Service service = this.services.get(t.name());
        if (null != service) {
            service.changeOrigin(t);
            return;
        }

        Object ret = Proxy.newProxyInstance(
                Configs.getConfigs(Configs.CLASSLOADER, CustomClassLoader.class),
                new Class[]{serviceClass},
                new ChangeOriginIH(t)
        );
        this.services.put(t.name(), (Service) ret);
    }

    public <T extends Service> T getService(Class<T> serviceClass, String name) {
        Service service = this.services.get(name);

        if (null != service) {
            return serviceClass.cast(service);
        }

        Object ret = Proxy.newProxyInstance(
                Configs.getConfigs(Configs.CLASSLOADER, CustomClassLoader.class),
                new Class[]{serviceClass},
                new ChangeOriginIH(null)
        );
        this.services.put(name, serviceClass.cast(ret));
        return serviceClass.cast(ret);
    }

    private Map<String, Controller> controllers = new HashMap<>();

    public <T extends Controller> void setController(Class<T> controllerClass) {
        T t = null;
        try {
            t = controllerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.catching(e);
            return;
        }

        Controller controller = this.controllers.get(t.name());
        if (null != controller) {
            controller.changeOrigin(t);
            return;
        }

        Object ret = Proxy.newProxyInstance(
                Configs.getConfigs(Configs.CLASSLOADER, CustomClassLoader.class),
                new Class[]{Controller.class},
                new ChangeOriginIH(t)
        );
        this.controllers.put(t.name(), (Controller) ret);
    }

    public Controller getController(String name) {
        Controller controller = this.controllers.get(name);

        if (null != controller) {
            return controller;
        }

        Object ret = Proxy.newProxyInstance(
                Configs.getConfigs(Configs.CLASSLOADER, CustomClassLoader.class),
                new Class[]{Controller.class},
                new ChangeOriginIH(null)
        );
        this.controllers.put(name, (Controller) ret);
        return (Controller) ret;
    }

    public <T extends EntityBeanI> T createBean(Class<T> clazz) {
        ClassLoader classLoader = Configs.getConfigs(Configs.CLASSLOADER, CustomClassLoader.class);
        if (null == classLoader) {
            classLoader = getClass().getClassLoader();
        }
        return clazz.cast(Proxy.newProxyInstance(
                classLoader,
                new Class[]{clazz},
                new DataAccessIH()
        ));
    }

    public <T extends EntityBeanI> T createBean(Class<T> clazz, String json) {
        T t = this.createBean(clazz);
        t.fromJSON(json);
        return t;
    }

}
