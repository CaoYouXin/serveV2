package auth;

import config.Configs;
import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class AuthHelper {

    public static final int ADMIN = 1;
    public static final int START_LOG = 1 << 1;

    private static final Map<Integer, BiFunction<HttpRequest, HttpContext, Boolean>> AUTH_MAP = new HashMap<>();

    public static void init() {
        AUTH_MAP.put(ADMIN, new AdminAuth());
        AUTH_MAP.put(START_LOG, new StartLogAuth());

        Configs.setConfigs(Configs.AUTH_MAP, AUTH_MAP);
    }

    public static Boolean auth(int auth, HttpRequest request, HttpContext context) {
        if (0 == auth) {
            return true;
        }

        AuthRuntimeException notAuth = null;
        boolean authPass = false;
        for (int aAuth : AUTH_MAP.keySet()) {
            if ((aAuth & auth) > 0) {
                try {
                    authPass = authPass || AUTH_MAP.get(aAuth).apply(request, context);
                } catch (AuthRuntimeException e) {
                    notAuth = e;
                }
            }
        }

        if (!authPass && null != notAuth) {
            throw notAuth;
        }
        return true;
    }

}
