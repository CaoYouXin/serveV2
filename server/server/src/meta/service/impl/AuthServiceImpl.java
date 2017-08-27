package meta.service.impl;

import beans.BeanManager;
import config.Configs;
import meta.data.EIAuth;
import meta.repository.IAuthRepo;
import meta.service.IAuthService;
import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.loader.CustomClassLoader;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class AuthServiceImpl implements IAuthService {

    private static final Logger logger = LogManager.getLogger(AuthServiceImpl.class);

    private IAuthRepo authRepo = BeanManager.getInstance().getRepository(IAuthRepo.class);

    @Override
    public Boolean setAuths(List<EIAuth> authList) {
        return authList.stream().allMatch((auth) -> {
            EIAuth byAuthType = authRepo.findByAuthType(auth.getAuthType());
            if (null == byAuthType) {
                byAuthType = auth;
            }
            byAuthType.setAuthClassName(auth.getAuthClassName());
            return authRepo.save(byAuthType);
        });
    }

    @Override
    public void initAuths() {
        List<EIAuth> all = this.authRepo.findAll();

        if (all.isEmpty()) {
            return;
        }

        CustomClassLoader classLoader = Configs.getConfigs(Configs.CLASSLOADER, CustomClassLoader.class);
        Map<Integer, BiFunction<HttpRequest, HttpContext, Boolean>> authMap = Configs.getConfigs(Configs.AUTH_MAP, Map.class);
        all.forEach(auth -> {
            try {
                Class<?> authClass = classLoader.loadClass(auth.getAuthClassName());
                authMap.put(auth.getAuthType(), (BiFunction<HttpRequest, HttpContext, Boolean>) authClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                logger.catching(e);
            }
        });
    }

    @Override
    public void before() {
        if (!this.authRepo.createTableIfNotExist()) {
            throw new RuntimeException("auth table can not be created");
        }
    }
}
