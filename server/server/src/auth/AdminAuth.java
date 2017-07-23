package auth;

import beans.BeanManager;
import config.Configs;
import meta.data.EIConfig;
import meta.repository.IConfigRepo;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpContext;

import java.util.function.BiFunction;

public class AdminAuth implements BiFunction<HttpRequest, HttpContext, Boolean> {

    private IConfigRepo configRepo = BeanManager.getInstance().getRepository(IConfigRepo.class);

    @Override
    public Boolean apply(HttpRequest request, HttpContext context) {
        Boolean withSchema = Configs.getConfigs(Configs.WITH_SCHEMA, Boolean.class);

        if (!withSchema) {
            return false;
        }

        Header header = request.getLastHeader("infinitely-serve-token");
        if (null == header) {
            throw new AuthRuntimeException("请求头中未包含【infinitely-serve-token】.");
        }

        if (!this.configRepo.createTableIfNotExist()) {
            throw new AuthRuntimeException("config table can not be created.");
        }

        EIConfig config = this.configRepo.findByConfigKey("admin.token");
        if (null == config) {
            throw new AuthRuntimeException("奇怪：管理员未登录.");
        }

        if (!header.getValue().equals(config.getConfigValue())) {
            throw new AuthRuntimeException("当前登录没有管理员权限.");
        }

        return true;
    }
}
