package beans;

import org.junit.jupiter.api.Test;
import proxy.TestProxyI;
import proxy.TestProxyImpl1;
import proxy.TestProxyImpl2;

import static org.junit.jupiter.api.Assertions.assertThrows;

class BeanManagerTest {

    @Test
    void test() {
//        BeanManager beanManager = BeanManager.getInstance();
//
//        beanManager.setService(TestProxyI.class, TestProxyImpl1.class);
//        TestProxyI bean = beanManager.getBean(TestProxyI.class);
//
//        bean.print();
//
//        beanManager.setService(TestProxyI.class, TestProxyImpl2.class);
//        bean.print();
    }

    @Test
    void testException() {
//        BeanManager beanManager = BeanManager.getInstance();
//        TestProxyI bean = beanManager.getBean(TestProxyI.class);
//        assertThrows(BeanNotInitException.class, bean::print);
    }

    @Test
    void testNotRecoverFromException() {
//        BeanManager beanManager = BeanManager.getInstance();
//        TestProxyI bean = beanManager.getBean(TestProxyI.class);
//        assertThrows(BeanNotInitException.class, () -> {
//            try {
//                bean.print();
//            } catch (OtherException e) {
//                beanManager.setService(TestProxyI.class, TestProxyImpl1.class);
//            } finally {
//                bean.print();
//            }
//        });
    }

    @Test
    void testRecoverFromException() {
//        BeanManager beanManager = BeanManager.getInstance();
//        TestProxyI bean = beanManager.getBean(TestProxyI.class);
//        try {
//            bean.print();
//        } catch (BeanNotInitException e) {
//            beanManager.setService(TestProxyI.class, TestProxyImpl1.class);
//        } finally {
//            bean.print();
//        }
    }

}
