package config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class Configs {

    public static final String OBJECT_MAPPER = "OBJECT_MAPPER";
    public static final String FILE_ROOT = "FILE_ROOT";
    public static final String META_APIS = "META_APIS";
    public static final String APIS = "APIS";
    public static final String CLASSLOADER = "CLASSLOADER";
    public static final String JAR_FILE_PATH = "JAR_FILE_PATH";
    public static final String SERVE_AUTH = "SERVE_AUTH";
    private static final Logger logger = LogManager.getLogger(Configs.class);

    /**
     * configs
     */
    private static Map<String, Object> CONFIGS = new HashMap<>();

    public static void setConfigs(String key, Object value) {
        CONFIGS.put(key, value);
    }

    public static <T> T getConfigs(String key, Class<T> clazz) {
        return getConfigs(key, clazz, null);
    }

    public static <T> T getConfigs(String key, Class<T> clazz, Callable<T> fallback) {
        Object ret = CONFIGS.get(key);
        if (null == ret && null != fallback) {
            try {
                ret = fallback.call();
            } catch (Exception e) {
                logger.catching(e);
            }
        }
        return clazz.cast(ret);
    }

}
