package proxy;

import beans.ChangeOriginIH;

import java.lang.reflect.Proxy;

public class TestProxyMain {

    public static void main(String[] args) {
        TestProxyImpl1 obj1 = new TestProxyImpl1();
        TestProxyI instance = (TestProxyI) Proxy.newProxyInstance(
                TestProxyMain.class.getClassLoader(), new Class[]{TestProxyI.class}, new ChangeOriginIH(obj1));
        instance.print();

        instance.changeOrigin(new TestProxyImpl2());
        instance.print();
    }

}
