package proxy;

import beans.ChangeOriginIH;
import org.junit.jupiter.api.Test;
import util.loader.CustomClassLoader;
import util.proxy.Proxy;

import java.net.URL;

class OwnProxy {

    @Test
    void test1() {
        TestProxyI proxy = TestProxyI.class.cast(Proxy.newProxyInstance(
                new CustomClassLoader(new URL[]{}, ClassLoader.getSystemClassLoader()),
                new Class[]{TestProxyI.class},
                new ChangeOriginIH(new TestProxyImpl1())
        ));
        proxy.print();
    }

}
