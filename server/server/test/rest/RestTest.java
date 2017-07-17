package rest;

import beans.BeanManager;
import org.junit.jupiter.api.Test;

class RestTest {

    @Test
    void serviceTest() {
//        BeanManager.getInstance().setService(Service1.class, Service1Impl.class);
        Service1 service1 = BeanManager.getInstance().getService(Service1.class);
        try {
            System.out.println(service1.getA());
        } catch (Exception e) {
            System.out.println(e instanceof RuntimeException);
            System.out.println(e.getMessage());
            System.out.println(e.getCause().getMessage());
            System.out.println(e.getCause().getCause().getMessage());
        }
    }

}
