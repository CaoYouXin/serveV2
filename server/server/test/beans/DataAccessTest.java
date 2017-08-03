package beans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import rest.JsonResponse;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Date;

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
            System.out.println(objectMapper.writeValueAsString(JsonResponse.success(bean.toMap())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    void test5() {
        ObjectMapper objectMapper = new ObjectMapper();
        Entity1 bean = BeanManager.getInstance().createBean(Entity1.class);
        bean.setA("AAA");

        try {
            System.out.println(objectMapper.writeValueAsString(JsonResponse.success(bean)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    void test6() {
        Entity1 bean = BeanManager.getInstance().createBean(Entity1.class, "{\"A\":\"AAA\"}");
        assertEquals("AAA", bean.getA());
    }

    @Test
    void test7() {
        DateEntity bean = BeanManager.getInstance().createBean(DateEntity.class, "{\"Date\":\"2017-01-01 12:00:00\"}");
        System.out.println(bean.getDate());

        DateEntity anotherBean = BeanManager.getInstance().createBean(DateEntity.class);
        anotherBean.setDate(new Date());
        System.out.println(anotherBean.getDate());
    }

    @Test
    void test8() {
        Entity1 bean = BeanManager.getInstance().createBean(Entity1.class, "{\"A\":\"AAA\"}");
        System.out.println(bean.getClass());
        System.out.println(Proxy.isProxyClass(bean.getClass()));
        System.out.println(Arrays.toString(bean.getClass().getInterfaces()));
    }

}
