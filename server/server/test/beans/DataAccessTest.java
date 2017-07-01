package beans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.Configs;
import org.junit.jupiter.api.Test;
import rest.JsonResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataAccessTest {

    @Test
    void test1() {
        Entity1 bean = BeanManager.getInstance().createBean(Entity1.class);
        bean.setA("aaa");
        assertEquals("aaa", bean.getA());
        bean.setA("aba");
        assertEquals("aba", bean.getA());
    }

    @Test
    void test2() {
        Entity1 bean = BeanManager.getInstance().createBean(Entity1.class);
        bean.setA("AAA");
        System.out.println(bean.toJSONString());
    }

    @Test
    void test3() {
        Entity1 bean1 = BeanManager.getInstance().createBean(Entity1.class);
        bean1.setA("should be equal");
        Entity1 bean2 = BeanManager.getInstance().createBean(Entity1.class);
        bean2.copyFrom(bean1);
        assertEquals(bean1.getA(), bean2.getA());
    }

    @Test
    void test4() {
        ObjectMapper objectMapper = new ObjectMapper();
        Entity1 bean = BeanManager.getInstance().createBean(Entity1.class);
        bean.setA("AAA");

        try {
            System.out.println(objectMapper.writeValueAsString(JsonResponse.success(bean)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
