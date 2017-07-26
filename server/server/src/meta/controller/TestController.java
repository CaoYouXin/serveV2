package meta.controller;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import rest.Controller;
import rest.JsonResponse;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestController extends WithMatcher implements Controller {

    private Class<?> lastClass = null;

    @Override
    public String name() {
        return "meta test";
    }

    @Override
    public String urlPattern() {
        return "/test/:date";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        Map<String, Object> ret = new HashMap<>();

        Map<String, String> params = this.getUriPatternMatcher().getParams(request);
        ret.put("param date", params.get("date"));

//        CustomClassLoader classLoader = Configs.getConfigs(Configs.CLASSLOADER, CustomClassLoader.class);
//        try {
//            Object bean = classLoader.loadClass("service.TestService").newInstance();
//            ret.put("bean", bean.toString());
//
//            boolean b1 = classLoader.loadClass("service.TestService")
//                    .equals(classLoader.loadClass("service.TestService"));
//            ret.put("ClassEquals", b1);
//
//            boolean b2 = classLoader.loadClass("service.TestService").equals(this.lastClass);
//            ret.put("Class Equals With Last", b2);
//            this.lastClass = classLoader.loadClass("service.TestService");
//
//        } catch (IllegalAccessException | InstantiationException e) {
//            e.printStackTrace();
//        }

        RestHelper.responseJSON(response, JsonResponse.success(ret));
    }
}
