package orm;

import config.Configs;

import java.lang.reflect.Proxy;

public class RepositoryManager {

    private static final RepositoryManager INSTANCE = new RepositoryManager();

    public static RepositoryManager getInstance() {
        return INSTANCE;
    }

    private RepositoryManager() {
    }

    public <T extends Repository> T buildRepository(Class<T> proto) {
        return proto.cast(Proxy.newProxyInstance(
                Configs.getConfigs(Configs.CLASSLOADER, ClassLoader.class, () -> getClass().getClassLoader()),
                new Class[]{proto},
                new RepositoryIH(proto)
        ));
    }

}
