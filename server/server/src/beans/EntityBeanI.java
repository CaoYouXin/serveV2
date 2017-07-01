package beans;

import java.util.Map;

public interface EntityBeanI {

    String toJSONString();
    void fromJSON(String json);
    Map<String, Object> toMap();
    void fromMap(Map<String, Object> map);
    void copyFrom(EntityBeanI entityBeanI);

}
