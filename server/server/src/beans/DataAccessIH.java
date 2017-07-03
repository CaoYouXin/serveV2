package beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.Configs;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DataAccessIH implements InvocationHandler {

    private Map<String, Object> data = new HashMap<>();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String name = method.getName();
        if (name.startsWith("get")) {
            return this.data.get(name.substring("get".length()));
        }

        if (name.startsWith("set")) {
            this.data.put(name.substring("set".length()), args[0]);
            return null;
        }

        if (name.startsWith("is")) {
            return this.data.get(name.substring("is".length()));
        }

        if (name.equals("toMap")) {
            return Collections.unmodifiableMap(this.data);
        }

        if (name.equals("fromMap")) {
            this.data = (Map<String, Object>) args[0];
            return null;
        }

        if (name.equals("toJSONString")) {
            ObjectMapper objectMapper = Configs.getConfigs(Configs.OBJECT_MAPPER, ObjectMapper.class, () -> new ObjectMapper());
            return objectMapper.writeValueAsString(this.data);
        }

        if (name.equals("fromJSON")) {
            ObjectMapper objectMapper = Configs.getConfigs(Configs.OBJECT_MAPPER, ObjectMapper.class, () -> new ObjectMapper());
            this.data = objectMapper.readValue((String) args[0], Map.class);
            return null;
        }

        if (name.equals("copyFrom")) {
            Map<String, Object> copyFrom = ((EntityBeanI) args[0]).toMap();
            this.data.putAll(copyFrom);
        }

        return null;
    }
}
