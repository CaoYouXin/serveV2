package beans;

import org.junit.jupiter.api.Test;

class EntityTest {

    @Test
    void extendsTest() {
        Entity2 bean = BeanManager.getInstance().createBean(Entity2.class, "{\"A\":\"smith\",\"B\":\"john\"}");
        System.out.println(bean.getA());
        System.out.println(bean.getB());
    }

    @Test
    void copyFormTest() {
        Entity3 bean3 = BeanManager.getInstance().createBean(Entity3.class);
        bean3.setA("a");
        bean3.setB("b");

        Entity1 bean1 = BeanManager.getInstance().createBean(Entity1.class);
        bean1.copyFrom(bean3);
        System.out.println(bean1.toJSONString());

        Entity1 anotherBean1 = BeanManager.getInstance().createBean(Entity1.class);
        anotherBean1.copyFromInclude(bean3, "A");
        System.out.println(anotherBean1.toJSONString());

        Entity2 bean2 = BeanManager.getInstance().createBean(Entity2.class);
        bean2.copyFromExclude(bean3, "A");
        System.out.println(bean2.toJSONString());

        Entity1 another1Bean1 = BeanManager.getInstance().createBean(Entity1.class);
        another1Bean1.copyFrom(bean3, Entity1.class);
        System.out.println(another1Bean1.toJSONString());
    }

}
