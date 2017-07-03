package orm.handler;

import orm.Query;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class QueryHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Query query = method.getDeclaredAnnotation(Query.class);
        if (null == query) {
            throw new RuntimeException("it has query just now.");
        }
        System.out.println(query.value());

        return null;
    }
}
