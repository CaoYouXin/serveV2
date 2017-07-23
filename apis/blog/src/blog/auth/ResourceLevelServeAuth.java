package blog.auth;

import auth.AuthRuntimeException;
import auth.ServeAuth;
import beans.BeanManager;
import blog.data.EIResourceLevelMapping;
import blog.data.EIUserToken;
import blog.repository.IResourceLevelMappingRepo;
import blog.repository.IUserTokenRepo;
import config.Configs;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpContext;
import rest.RestHelper;

import java.util.Date;
import java.util.List;

public class ResourceLevelServeAuth implements ServeAuth {

    public static final String RESOURCE_LEVEL_SERVE_AUTH_CONFIG_KEY = "RESOURCE_LEVEL_SERVE_AUTH_CONFIG_KEY";

    private IResourceLevelMappingRepo resourceLevelMappingRepo = BeanManager.getInstance().getRepository(IResourceLevelMappingRepo.class);
    private IUserTokenRepo userTokenRepo = BeanManager.getInstance().getRepository(IUserTokenRepo.class);

    private List<EIResourceLevelMapping> eiResourceLevelMappingList;

    @Override
    public Boolean apply(HttpRequest httpRequest, HttpContext httpContext) {
        Boolean withSchema = Configs.getConfigs(Configs.WITH_SCHEMA, Boolean.class);

        if (!withSchema) {
            return false;
        }

        this.checkTableExistence();

        EIResourceLevelMapping matchedMapping = this.getMatchedMapping(httpRequest);
        if (null == matchedMapping) {
            return true;
        }

        Long userId = this.getUserId(httpRequest);

        return null;
    }

    private Long getUserId(HttpRequest httpRequest) {
        Header header = httpRequest.getLastHeader("infinitely-serve-token");
        if (null == header) {
            throw new AuthRuntimeException("请求头中未包含【infinitely-serve-token】.");
        }

        EIUserToken eiUserToken = this.userTokenRepo.findByUserToken(header.getValue());
        if (null == eiUserToken) {
            throw new AuthRuntimeException("没有登录信息");
        }

        if (eiUserToken.getUserTokenDeadTime().before(new Date())) {
            throw new AuthRuntimeException("Token过期，请重新登录");
        }

        return eiUserToken.getUserId();
    }

    private EIResourceLevelMapping getMatchedMapping(HttpRequest httpRequest) {
        if (null == this.eiResourceLevelMappingList || !Configs.getConfigs(RESOURCE_LEVEL_SERVE_AUTH_CONFIG_KEY, Boolean.class)) {
            this.eiResourceLevelMappingList = this.resourceLevelMappingRepo.findAllByResourceLevelMappingDisabled(false);
            Configs.setConfigs(RESOURCE_LEVEL_SERVE_AUTH_CONFIG_KEY, true);
        }

        int matchLength = -1;
        EIResourceLevelMapping match = null;
        String decodedUrl = RestHelper.getDecodedUrl(httpRequest);
        for (EIResourceLevelMapping eiResourceLevelMapping : this.eiResourceLevelMappingList) {
            if (eiResourceLevelMapping.getResourceUrlPrefix().length() > matchLength
                    && decodedUrl.startsWith(eiResourceLevelMapping.getResourceUrlPrefix())) {
                match = eiResourceLevelMapping;
            }
        }

        return match;
    }

    private void checkTableExistence() {
        if (!this.resourceLevelMappingRepo.createTableIfNotExist()) {
            throw new AuthRuntimeException("resource level mapping table can not be created");
        }

        if (!this.userTokenRepo.createTableIfNotExist()) {
            throw new AuthRuntimeException("user token table can not be created");
        }
    }


}
