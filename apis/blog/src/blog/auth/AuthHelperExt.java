package blog.auth;

import auth.AuthHelper;
import beans.BeanManager;
import config.Configs;
import meta.data.EIAuth;
import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class AuthHelperExt extends AuthHelper {

    public static final int BLOG_LOGIN = 1 << 2;

    public static List<EIAuth> addExtAuth() {
        Map<Integer, BiFunction<HttpRequest, HttpContext, Boolean>> authMap = Configs.getConfigs(Configs.AUTH_MAP, Map.class);

        authMap.put(BLOG_LOGIN, new BlogLoginAuth());

        List<EIAuth> authList = new ArrayList<>();

        EIAuth blogLoginAuth = BeanManager.getInstance().createBean(EIAuth.class);
        blogLoginAuth.setAuthType(BLOG_LOGIN);
        blogLoginAuth.setAuthClassName(BlogLoginAuth.class.getName());
        authList.add(blogLoginAuth);

        return authList;
    }

}
