package beans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import orm.Repository;
import orm.RepositoryManager;
import rest.Controller;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class BeanManager {

    private static final Logger logger = LogManager.getLogger(BeanManager.class);

    private Map<Class<?>, ChangeOriginI> beans = new HashMap<>();

    public <T extends ChangeOriginI> void setService(Class<T> key, Class<? extends T> origin) {
        T t = null;
        try {
            t = origin.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.catching(e);
        }

        if (null == t) {
            return;
        }

        ChangeOriginI changeOriginI = this.beans.get(key);
        if (null != changeOriginI) {
            changeOriginI.changeOrigin(t);
            return;
        }

        Object ret = Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{key}, new ChangeOriginIH(t));
        this.beans.put(key, (ChangeOriginI) ret);
    }

    public <T extends Repository> void setRepository(Class<? extends T> key) {
        RepositoryManager repositoryManager = RepositoryManager.getInstance();
        T t = repositoryManager.buildRepository(key);

        ChangeOriginI changeOriginI = this.beans.get(key);
        if (null != changeOriginI) {
            changeOriginI.changeOrigin(t);
            return;
        }

        Object ret = Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{key}, new ChangeOriginIH(t));
        this.beans.put(key, (ChangeOriginI) ret);
    }

    public <T extends ChangeOriginI> T getBean(Class<? extends T> key) {
        ChangeOriginI changeOriginI = this.beans.get(key);
        if (null != changeOriginI) {
            return (T) changeOriginI;
        }

        Object ret = Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{key}, new ChangeOriginIH(null));
        this.beans.put(key, (ChangeOriginI) ret);
        return (T) ret;
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
                getClass().getClassLoader(),
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
                getClass().getClassLoader(),
                new Class[]{Controller.class},
                new ChangeOriginIH(null)
        );
        this.controllers.put(name, (Controller) ret);
        return (Controller) ret;
    }

}
