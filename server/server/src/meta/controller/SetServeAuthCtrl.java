package meta.controller;

import auth.AuthHelper;
import auth.DefaultServeAuth;
import auth.ServeAuth;
import beans.BeanManager;
import config.Configs;
import meta.data.EIConfig;
import meta.repository.IConfigRepo;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rest.HelperController;
import rest.JsonResponse;
import rest.RestHelper;
import util.loader.CustomClassLoader;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class SetServeAuthCtrl extends HelperController {

    private static final Logger logger = LogManager.getLogger(SetServeAuthCtrl.class);

    private IConfigRepo configRepo = BeanManager.getInstance().getRepository(IConfigRepo.class);

    @Override
    public int auth() {
        return AuthHelper.ADMIN;
    }

    @Override
    public String name() {
        return "meta set serve auth";
    }

    @Override
    public String urlPattern() {
        return "/serve/auth/:className";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        Map<String, String> params = this.getUriPatternMatcher().getParams(request);
        String className = params.get("className");

        if ("".equals(className)) {
            this.setConfig("");
            Configs.setConfigs(Configs.SERVE_AUTH, new DefaultServeAuth());
            RestHelper.responseJSON(response, JsonResponse.success("操作成功"));
            return;
        }

        CustomClassLoader classLoader = Configs.getConfigs(Configs.CLASSLOADER, CustomClassLoader.class);
        Object serveAuth = null;
        try {
            Objects.requireNonNull(classLoader);
            serveAuth = classLoader.loadClass(className).newInstance();
        } catch (InstantiationException | IllegalAccessException | NullPointerException e) {
            logger.catching(e);
            RestHelper.responseJSON(response, JsonResponse.fail(50006, "无法创建 ServeAuth 实例。"));
            return;
        }

        if (serveAuth instanceof ServeAuth) {
            this.setConfig(className);
            Configs.setConfigs(Configs.SERVE_AUTH, serveAuth);
            RestHelper.responseJSON(response, JsonResponse.success("操作成功"));
        } else {
            RestHelper.responseJSON(response, JsonResponse.fail(50006, "并没有选择 ServeAuth 实例。"));
        }
    }

    private void setConfig(String className) {
        if (!this.configRepo.createTableIfNotExist()) {
            return;
        }

        EIConfig config = this.configRepo.findByConfigKey("serve.auth");
        if (null == config) {
            config = BeanManager.getInstance().createBean(EIConfig.class);
            config.setConfigKey("serve.auth");
        }
        config.setConfigValue(className);

        this.configRepo.save(config);
    }
}
