package beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.Configs;
import util.StringUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataAccessIH implements InvocationHandler {

    private Map<String, Object> data = new HashMap<>();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String name = method.getName();
        if (name.startsWith("get")) {
            Object value = this.data.get(name.substring("get".length()));

            switch (method.getReturnType().getTypeName()) {
                case "java.util.Date":
                    if (value instanceof Date) {
                        return value;
                    }

                    DateFormat dateFormat = Configs.getConfigs(Configs.DATE_FORMAT, DateFormat.class, () -> {
                        DateFormat fallback = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        fallback.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

                        return fallback;
                    });
                    return dateFormat.parse((String) value);
                default:
                    return value;
            }
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
            if (args.length == 1) {
                this.data.putAll(copyFrom);
                return null;
            }
            if (args.length == 2) {
                this.data.putAll(this.filter(copyFrom, (Class<?>) args[1]));
                return null;
            }
        }

        if (name.equals("copyFromInclude")) {
            this.data.putAll(this.filter(args, true));
            return null;
        }

        if (name.equals("copyFromExclude")) {
            this.data.putAll(this.filter(args, false));
            return null;
        }

        return null;
    }

    private Map<String, Object> filter(Map<String, Object> source, Class<?> template) {
        Map<String, Object> ret = new HashMap<>();
        for (Method method : template.getMethods()) {
            if (!method.getName().startsWith("set")) {
                continue;
            }

//            System.out.println(method.getName());

            String key = StringUtil.cutPrefix(method.getName(), "set");
            ret.put(key, source.get(key));
        }
        return ret;
    }

    private Map<String, Object> filter(Object[] args, boolean includeMode) {
        Map<String, Object> ret = new HashMap<>();
        Map<String, Object> source = ((EntityBeanI) args[0]).toMap();

        List<String> keyList = Arrays.asList((String[]) args[1]);

        for (String key : source.keySet()) {
            boolean contains = keyList.contains(key);

//            System.out.println(key);
//            System.out.println(contains);
//            System.out.println(includeMode == contains);

            if (includeMode == contains) {
//                System.out.println(source.get(key));
                ret.put(key, source.get(key));
//                System.out.println(ret.get(key));
            }
        }
        return ret;
    }

}
