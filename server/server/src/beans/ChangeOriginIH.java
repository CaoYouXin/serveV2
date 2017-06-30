package beans;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ChangeOriginIH implements InvocationHandler {

    private Object object;

    public ChangeOriginIH(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("changeOrigin")) {
            this.object = args[0];
        }

        if (null != this.object) {
            return method.invoke(this.object, args);
        }

        throw new BeanNotInitException("proxy object is null.");
    }
}
