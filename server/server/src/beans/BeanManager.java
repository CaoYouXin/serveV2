package beans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import orm.Repository;
import orm.RepositoryManager;
import orm.RepositoryName;
import rest.Controller;
import rest.Service;

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

    private Map<String, Repository> repositories = new HashMap<>();

    public <T extends Repository> void setRepository(Class<? extends T> repositoryClass) {
        RepositoryName declaredAnnotation = repositoryClass.getDeclaredAnnotation(RepositoryName.class);
        String repoName = declaredAnnotation.value();

        T t = RepositoryManager.getInstance().buildRepository(repositoryClass);

        Repository repository = this.repositories.get(repoName);
        if (null != repository) {
            repository.changeOrigin(t);
            return;
        }

        Object ret = Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Repository.class},
                new ChangeOriginIH(t)
        );
        this.repositories.put(repoName, (Repository) ret);
    }

    public Repository getRepository(String name) {
        Repository repository = this.repositories.get(name);

        if (null != repository) {
            return repository;
        }

        Object ret = Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Repository.class},
                new ChangeOriginIH(null)
        );
        this.repositories.put(name, (Repository) ret);
        return (Repository) ret;
    }

    private Map<String, Service> services = new HashMap<>();

    public <T extends Service> void setService(Class<T> serviceClass) {
        T t = null;
        try {
            t = serviceClass.newInstance();
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
                getClass().getClassLoader(),
                new Class[]{Service.class},
                new ChangeOriginIH(t)
        );
        this.services.put(t.name(), (Service) ret);
    }

    public Service getService(String name) {
        Service service = this.services.get(name);

        if (null != service) {
            return service;
        }

        Object ret = Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Service.class},
                new ChangeOriginIH(null)
        );
        this.services.put(name, (Service) ret);
        return (Service) ret;
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

    public <T extends EntityBeanI> T createBean(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{clazz},
                new DataAccessIH()
        );
    }

}
