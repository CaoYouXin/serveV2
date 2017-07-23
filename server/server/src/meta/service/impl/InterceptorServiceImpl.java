package meta.service.impl;

import beans.BeanManager;
import config.Configs;
import meta.data.EIInterceptor;
import meta.repository.IInterceptorRepo;
import meta.service.IInterceptorService;
import meta.service.exp.InterceptorSetException;
import rest.Interceptor;
import util.loader.CustomClassLoader;

import java.util.List;

public class InterceptorServiceImpl implements IInterceptorService {

    private IInterceptorRepo interceptorRepo = BeanManager.getInstance().getRepository(IInterceptorRepo.class);

    @Override
    public void before() {
        if (!this.interceptorRepo.createTableIfNotExist()) {
            throw new RuntimeException("interceptor table cannot be created.");
        }
    }

    @Override
    public List<EIInterceptor> list() {
        return this.interceptorRepo.findAllByInterceptorDisabled(false);
    }

    @Override
    public EIInterceptor save(String className) throws InterceptorSetException {
        CustomClassLoader classLoader = Configs.getConfigs(Configs.CLASSLOADER, CustomClassLoader.class);
        Interceptor interceptorBean = null;
        try {
            interceptorBean = (Interceptor) classLoader.loadClass(className).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new InterceptorSetException("cannot initialize interceptor.");
        }

        if (null == interceptorBean.name()) {
            throw new InterceptorSetException("cannot initialize interceptor.");
        }

        EIInterceptor interceptor = this.interceptorRepo.findByInterceptorName(interceptorBean.name());
        if (null == interceptor) {
            interceptor = BeanManager.getInstance().createBean(EIInterceptor.class);
            interceptor.setInterceptorName(interceptorBean.name());
            interceptor.setInterceptorDisabled(false);
        }
        interceptor.setPostInterceptor(interceptorBean.isPost());
        interceptor.setInterceptorClassName(className);

        if (!this.interceptorRepo.save(interceptor)) {
            throw new InterceptorSetException("cannot save interceptor");
        }

        Configs.setConfigs(Interceptor.INTERCEPTOR_CONFIG_KEY, false);
        return interceptor;
    }

    @Override
    public Boolean disabled(Long id, Boolean disabled) throws InterceptorSetException {
        EIInterceptor eiInterceptor = this.interceptorRepo.find(id);
        if (null == eiInterceptor) {
            throw new InterceptorSetException("没有相应的拦截器.");
        }

        eiInterceptor.setInterceptorDisabled(disabled);

        if (!this.interceptorRepo.save(eiInterceptor)) {
            throw new InterceptorSetException("无法保存拦截器状态.");
        }

        Configs.setConfigs(Interceptor.INTERCEPTOR_CONFIG_KEY, false);
        return disabled;
    }
}
