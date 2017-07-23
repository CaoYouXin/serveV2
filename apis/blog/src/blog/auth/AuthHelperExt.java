package blog.auth;

import auth.AuthHelper;
import config.Configs;
import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpContext;

import java.util.Map;
import java.util.function.BiFunction;

public class AuthHelperExt extends AuthHelper {

    public static final int BLOG_LOGIN = 3;

    public static void addExtAuth() {
        Map<Integer, BiFunction<HttpRequest, HttpContext, Boolean>> authMap = Configs.getConfigs(Configs.AUTH_MAP, Map.class);


    }

}
