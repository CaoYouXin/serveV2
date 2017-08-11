package blog.auth;

import auth.AuthRuntimeException;
import beans.BeanManager;
import blog.data.EIUserToken;
import blog.repository.IUserTokenRepo;
import config.Configs;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.function.BiFunction;

public class BlogLoginAuth implements BiFunction<HttpRequest, HttpContext, Boolean> {

    public static final String USER_ID_KEY = "infinitely-serve-user_id";
    private static final Logger logger = LogManager.getLogger(BlogLoginAuth.class);
    private IUserTokenRepo userTokenRepo = BeanManager.getInstance().getRepository(IUserTokenRepo.class);

    @Override
    public Boolean apply(HttpRequest httpRequest, HttpContext httpContext) {
        Boolean withSchema = Configs.getConfigs(Configs.WITH_SCHEMA, Boolean.class);

        if (!withSchema) {
            return false;
        }

        Header header = httpRequest.getLastHeader("infinitely-serve-token");
        if (null == header) {
            throw new AuthRuntimeException("请求头中未包含【infinitely-serve-token】.");
        }

        if (!this.userTokenRepo.createTableIfNotExist()) {
            throw new AuthRuntimeException("user token table can not be created");
        }

        EIUserToken eiUserToken = this.userTokenRepo.findByUserToken(header.getValue());
        if (null == eiUserToken) {
            throw new AuthRuntimeException("没有登录信息");
        }

        if (eiUserToken.getUserTokenDeadTime().before(new Date())) {
            throw new AuthRuntimeException("Token过期，请重新登录");
        }

        logger.info(String.format("userid : %s", eiUserToken.getUserId() + ""));
        httpContext.setAttribute(USER_ID_KEY, eiUserToken.getUserId());
        return true;
    }
}
