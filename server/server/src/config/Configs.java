package config;

import java.util.HashMap;
import java.util.Map;

public class Configs {

    public static final String OBJECT_MAPPER = "OBJECT_MAPPER";
    public static final String FILE_ROOT = "FILE_ROOT";
    public static final String META_APIS = "META_APIS";
    public static final String APIS = "APIS";

    /**
     * configs
     */
    private static Map<String, Object> CONFIGS = new HashMap<>();

    public static void setConfigs(String key, Object value) {
        CONFIGS.put(key, value);
    }

    public static <T> T getConfigs(String key, Class<T> clazz) {
        return (T) CONFIGS.get(key);
    }

}
