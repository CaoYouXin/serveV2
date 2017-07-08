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
import service.ITestService;
import util.loader.CustomClassLoader;

import java.io.IOException;

public class TestController extends HelperController implements Controller {

    static {

    }

    private ITestService service = BeanManager.getInstance().getService(ITestService.class);

    @Override
    public String name() {
        return "controller.TestController";
    }

    @Override
    public String urlPattern() {
        return "/test";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {

        CustomClassLoader classLoader = Configs.getConfigs(Configs.CLASSLOADER, CustomClassLoader.class);
        BeanManager.getInstance().setService(
                (Class<ITestService>) classLoader.loadClass("service.ITestService"),
                (Class<ITestService>) classLoader.loadClass("service.impl.TestService")
        );

        RestHelper.responseJSON(response, JsonResponse.success(this.service.test()));
    }
}
