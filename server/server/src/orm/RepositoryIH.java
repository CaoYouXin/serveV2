package orm;

import config.Configs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import orm.handler.*;
import util.StringUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RepositoryIH implements InvocationHandler {

    private static final Logger logger = LogManager.getLogger(RepositoryIH.class);

    private static final String[] GENERIC_NAMES = {"TABLE", "ID"};

    private final Class<? extends Repository> clazz;
    private Map<String, Class<?>> genericParams;
    private Map<String, InvocationHandler> invocationHandlerMap;

    public RepositoryIH(Class<? extends Repository> clazz) {
        this.clazz = clazz;
        this.genericParams = new HashMap<>();
        this.invocationHandlerMap = new HashMap<>();

        this.parseGenericParams(this.clazz.getGenericInterfaces(),
                this.clazz.getInterfaces(),
                Configs.getConfigs(
                Configs.CLASSLOADER, ClassLoader.class,
                () -> getClass().getClassLoader()
        ));
        this.registerInvocationHandlers();
    }

    private void registerInvocationHandlers() {
        this.invocationHandlerMap.put("find", new FindHandler(this.genericParams));
        this.invocationHandlerMap.put("save", new SaveHandler(this.genericParams.get(GENERIC_NAMES[0])));
        this.invocationHandlerMap.put("remove", new RemoveHandler(this.genericParams.get(GENERIC_NAMES[0])));
        this.invocationHandlerMap.put("query", new QueryHandler());
        this.invocationHandlerMap.put("createTableIfNotExist", new DDLHandler(GENERIC_NAMES, this.genericParams));

        this.invocationHandlerMap = Collections.unmodifiableMap(this.invocationHandlerMap);
    }

    private void parseGenericParams(Type[] genericInterfaces, Class<?>[] interfaces, ClassLoader classLoader) {
        if (genericInterfaces.length != interfaces.length) {
            throw new RuntimeException("不会吧");
        }

        for (int i = 0; i < genericInterfaces.length; i++) {
            Type genericInterface = genericInterfaces[i];
            String typeName = genericInterface.getTypeName();

            if (!typeName.startsWith("orm.Repository")) {
                this.parseGenericParams(interfaces[i].getGenericInterfaces(), interfaces[i].getInterfaces(), classLoader);
                continue;
            }

            int start = "orm.Repository<".length(),
                    end = StringUtil.indexOf(typeName, start, ',', '>'),
                    j = 0;

            while (-1 != end) {
                String className = typeName.substring(start, end);
                try {
                    this.genericParams.put(GENERIC_NAMES[j++], classLoader.loadClass(className));
                } catch (ClassNotFoundException e) {
                    logger.catching(e);
                }

                start = end + 2;
                end = StringUtil.indexOf(typeName, start, ',', '>');
            }
        }

        this.genericParams = Collections.unmodifiableMap(this.genericParams);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        InvocationHandler invocationHandler = this.invocationHandlerMap.get(methodName);
        if (null != invocationHandler) {
            return invocationHandler.invoke(proxy, method, args);
        }

        Query query = method.getDeclaredAnnotation(Query.class);
        if (null != query) {
            invocationHandler = this.invocationHandlerMap.get("query");
            if (null != invocationHandler) {
                return invocationHandler.invoke(proxy, method, args);
            }
        }

        if (methodName.startsWith("find")) {
            invocationHandler = this.invocationHandlerMap.get("find");
            if (null != invocationHandler) {
                return invocationHandler.invoke(proxy, method, args);
            }
        }

        if (methodName.startsWith("remove") || methodName.startsWith("softRemove")) {
            invocationHandler = this.invocationHandlerMap.get("remove");
            if (null != invocationHandler) {
                return invocationHandler.invoke(proxy, method, args);
            }
        }

        return null;
    }
}
