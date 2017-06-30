package proxy;

import beans.ChangeOriginIH;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

class TestProxy {

    @Test
    void test() {
        TestProxyImpl1 obj1 = new TestProxyImpl1();
        TestProxyI instance = (TestProxyI) Proxy.newProxyInstance(
                getClass().getClassLoader(), new Class[]{TestProxyI.class}, new ChangeOriginIH(obj1));
        instance.print();

        instance.changeOrigin(new TestProxyImpl2());
        instance.print();
    }

}
