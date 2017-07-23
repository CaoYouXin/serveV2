package meta.service;

import meta.data.EIInterceptor;
import meta.service.exp.InterceptorSetException;
import rest.Service;

import java.util.List;

public interface IInterceptorService extends Service {

    List<EIInterceptor> list();

    EIInterceptor save(String className) throws InterceptorSetException;

    Boolean disabled(Long id, Boolean disabled) throws InterceptorSetException;

}
