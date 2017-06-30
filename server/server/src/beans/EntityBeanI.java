package beans;

import java.util.Map;

public interface EntityBeanI {

    String toJSONString();
    Map<String, Object> toMap();
    void copyFrom(EntityBeanI entityBeanI);

}
