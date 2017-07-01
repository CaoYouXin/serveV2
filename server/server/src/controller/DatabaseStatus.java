package controller;

import beans.BeanManager;
import config.Configs;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.Controller;
import rest.HelperController;
import rest.JsonResponse;
import rest.RestHelper;
import util.ClasspathHacker;
import util.loader.CustomClassLoader;

import java.io.IOException;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class DatabaseStatus extends HelperController implements Controller {

    private Class<?> lastClass = null;

    @Override
    public String name() {
        return "database status";
    }

    @Override
    public String urlPattern() {
        return "/database/:date/status";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        Map<String, Object> ret = new HashMap<>();

        Map<String, String> params = this.getUriPatternMatcher().getParams(request);
        ret.put("param date", params.get("date"));

        CustomClassLoader classLoader = Configs.getConfigs(Configs.CLASSLOADER, CustomClassLoader.class);
        try {
            Object bean = classLoader.loadClass("service.impl.TestService1").newInstance();
            ret.put("bean", bean.toString());

            boolean b1 = classLoader.loadClass("service.impl.TestService1")
                    .equals(classLoader.loadClass("service.impl.TestService1"));
            ret.put("ClassEquals", b1);

            boolean b2 = classLoader.loadClass("service.impl.TestService1").equals(this.lastClass);
            ret.put("Class Equals With Last", b2);
            this.lastClass = classLoader.loadClass("service.impl.TestService1");

        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        RestHelper.responseJSON(response, JsonResponse.success(ret));
    }
}
