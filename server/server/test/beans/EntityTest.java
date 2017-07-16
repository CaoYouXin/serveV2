package beans;

import org.junit.jupiter.api.Test;

class EntityTest {

    @Test
    void extendsTest() {
        Entity2 bean = BeanManager.getInstance().createBean(Entity2.class, "{\"A\":\"smith\",\"B\":\"john\"}");
        System.out.println(bean.getA());
        System.out.println(bean.getB());
    }

}
