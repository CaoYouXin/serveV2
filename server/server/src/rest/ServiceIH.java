package rest;

import beans.ChangeOriginIH;

import java.lang.reflect.Method;

public class ServiceIH extends ChangeOriginIH {
    public ServiceIH(Object object) {
        super(object);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (this.object instanceof Service) {
            ((Service) this.object).before();
        }

        return super.invoke(proxy, method, args);
    }
}
