package beans;

import config.Configs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import orm.Repository;
import orm.RepositoryManager;
import rest.Controller;
import rest.Service;
import util.loader.CustomClassLoader;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class BeanManager {

    private static final Logger logger = LogManager.getLogger(BeanManager.class);

    private static final BeanManager INSTANCE = new BeanManager();
    private Map<Class<? extends Repository>, Repository> repositories = new HashMap<>();
    private Map<Class<? extends Service>, Service> services = new HashMap<>();
    private Map<Class<? extends Service>, Class<? extends Service>> serviceCache = new HashMap<>();
    private Map<String, Controller> controllers = new HashMap<>();

    private BeanManager() {
    }

    public static BeanManager getInstance() {
        return INSTANCE;
    }

    public <T extends Repository> T getRepository(Class<T> proto) {
        Repository repository = this.repositories.get(proto);
        if (null != repository) {
            return proto.cast(repository);
        }

        T buildRepository = RepositoryManager.getInstance().buildRepository(proto);
        this.repositories.put(proto, buildRepository);
        return buildRepository;
    }

    public <T extends Service, Impl extends T> void setService(Class<T> serviceClass, Class<Impl> serviceImplClass) {
        Class<? extends Service> cachedImplClass = this.serviceCache.get(serviceClass);
        if (serviceImplClass.equals(cachedImplClass)) {
            return;
        }
        this.serviceCache.put(serviceClass, serviceImplClass);

        T t = null;
        try {
            t = serviceImplClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.catching(e);
            return;
        }

        Service service = this.services.get(serviceClass);
        if (null != service) {
            service.changeOrigin(t);
            return;
        }

        Object ret = Proxy.newProxyInstance(
                Configs.getConfigs(Configs.CLASSLOADER, CustomClassLoader.class),
                new Class[]{serviceClass},
                new ChangeOriginIH(t)
        );
        this.services.put(serviceClass, (Service) ret);
    }

    public <T extends Service> T getService(Class<T> serviceClass) {
        Service service = this.services.get(serviceClass);

        if (null != service) {
            return serviceClass.cast(service);
        }

        Object ret = Proxy.newProxyInstance(
                Configs.getConfigs(Configs.CLASSLOADER, CustomClassLoader.class),
                new Class[]{serviceClass},
                new ChangeOriginIH(null)
        );
        this.services.put(serviceClass, serviceClass.cast(ret));
        return serviceClass.cast(ret);
    }

    public <T extends Controller> String setController(Class<T> controllerClass) {
        T t = null;
        try {
            t = controllerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.catching(e);
            return null;
        }

        Controller controller = this.controllers.get(t.name());
        if (null != controller) {
            controller.changeOrigin(t);
            return t.name();
        }

        Object ret = Proxy.newProxyInstance(
                Configs.getConfigs(Configs.CLASSLOADER, CustomClassLoader.class),
                new Class[]{Controller.class},
                new ChangeOriginIH(t)
        );
        this.controllers.put(t.name(), (Controller) ret);
        return t.name();
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
