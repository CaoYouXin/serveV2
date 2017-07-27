package beans;

import java.util.Map;

public interface EntityBeanI {

    String toJSONString();

    void fromJSON(String json);

    Map<String, Object> toMap();

    void fromMap(Map<String, Object> map);

    void copyFrom(EntityBeanI entityBeanI);

    void copyFrom(EntityBeanI entityBeanI, Class<?> template);

    void copyFromInclude(EntityBeanI entityBeanI, String... includeKeys);

    void copyFromExclude(EntityBeanI entityBeanI, String... excludeKeys);

}
