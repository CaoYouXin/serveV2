package orm;

import config.Configs;
import util.loader.CustomClassLoader;

import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;

public class RepositoryManager {

    private static final RepositoryManager INSTANCE = new RepositoryManager();

    public static RepositoryManager getInstance() {
        return INSTANCE;
    }

    private RepositoryManager() {
    }

    public <T extends Repository> T buildRepository(Class<T> proto) {
        return (T) Proxy.newProxyInstance(
                Configs.getConfigs(Configs.CLASSLOADER, ClassLoader.class, () -> getClass().getClassLoader()),
                new Class[]{proto},
                new RepositoryIH(proto)
        );
    }

}
