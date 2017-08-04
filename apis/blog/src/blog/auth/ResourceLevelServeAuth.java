package blog.auth;

import auth.AuthRuntimeException;
import auth.ServeAuth;
import beans.BeanManager;
import blog.data.EIResourceLevel;
import blog.data.EIUserToken;
import blog.repository.IResourceLevelMappingRepo;
import blog.repository.IResourceLevelRepo;
import blog.repository.IUserFavourRepo;
import blog.repository.IUserTokenRepo;
import blog.view.EIResourceLevelMappingDetail;
import config.Configs;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpContext;
import rest.RestHelper;

import java.util.Date;
import java.util.List;

public class ResourceLevelServeAuth implements ServeAuth {

    public static final String RESOURCE_LEVEL_SERVE_AUTH_CONFIG_KEY = "RESOURCE_LEVEL_SERVE_AUTH_CONFIG_KEY";

    private IResourceLevelRepo resourceLevelRepo = BeanManager.getInstance().getRepository(IResourceLevelRepo.class);
    private IResourceLevelMappingRepo resourceLevelMappingRepo = BeanManager.getInstance().getRepository(IResourceLevelMappingRepo.class);
    private IUserTokenRepo userTokenRepo = BeanManager.getInstance().getRepository(IUserTokenRepo.class);
    private IUserFavourRepo userFavourRepo = BeanManager.getInstance().getRepository(IUserFavourRepo.class);

    private List<EIResourceLevelMappingDetail> eiResourceLevelMappingDetailList;

    @Override
    public Boolean apply(HttpRequest httpRequest, HttpContext httpContext) {
        Boolean withSchema = Configs.getConfigs(Configs.WITH_SCHEMA, Boolean.class);

        if (!withSchema) {
            return false;
        }

        this.checkTableExistence();

        EIResourceLevelMappingDetail matchedMapping = this.getMatchedMapping(httpRequest);
        if (null == matchedMapping) {
            return true;
        }

        Long userId = this.getUserId(httpRequest);
        if (null == userId) {
            throw new AuthRuntimeException(matchedMapping.getResourceLevelExpMsg());
        }

        List<EIResourceLevel> eiResourceLevels = this.resourceLevelRepo.queryByUserId(userId);
        for (EIResourceLevel eiResourceLevel : eiResourceLevels) {
            if (eiResourceLevel.getResourceLevelId().equals(matchedMapping.getResourceLevelId())) {
                return true;
            }
        }

        throw new AuthRuntimeException(matchedMapping.getResourceLevelExpMsg());
    }

    private Long getUserId(HttpRequest httpRequest) {
        Header header = httpRequest.getLastHeader("infinitely-serve-token");
        if (null == header) {
            return null;
        }

        EIUserToken eiUserToken = this.userTokenRepo.findByUserToken(header.getValue());
        if (null == eiUserToken) {
            return null;
        }

        if (eiUserToken.getUserTokenDeadTime().before(new Date())) {
            return null;
        }

        return eiUserToken.getUserId();
    }

    private EIResourceLevelMappingDetail getMatchedMapping(HttpRequest httpRequest) {
        if (null == this.eiResourceLevelMappingDetailList || !Configs.getConfigs(RESOURCE_LEVEL_SERVE_AUTH_CONFIG_KEY, Boolean.class)) {
            this.eiResourceLevelMappingDetailList = this.resourceLevelMappingRepo.queryAll();
            Configs.setConfigs(RESOURCE_LEVEL_SERVE_AUTH_CONFIG_KEY, true);
        }

        int matchLength = -1;
        EIResourceLevelMappingDetail match = null;
        String decodedUrl = RestHelper.getDecodedUrl(httpRequest);
        for (EIResourceLevelMappingDetail eiResourceLevelMappingDetail : this.eiResourceLevelMappingDetailList) {
            if (eiResourceLevelMappingDetail.getResourceUrlPrefix().length() > matchLength
                    && decodedUrl.startsWith(eiResourceLevelMappingDetail.getResourceUrlPrefix())) {
                match = eiResourceLevelMappingDetail;
                matchLength = eiResourceLevelMappingDetail.getResourceUrlPrefix().length();
            }
        }

        return match;
    }

    private void checkTableExistence() {
        if (!this.resourceLevelRepo.createTableIfNotExist()) {
            throw new AuthRuntimeException("resource level table can not be created");
        }

        if (!this.userFavourRepo.createTableIfNotExist()) {
            throw new AuthRuntimeException("user favour mapping table can not be created");
        }

        if (!this.resourceLevelMappingRepo.createTableIfNotExist()) {
            throw new AuthRuntimeException("resource level mapping table can not be created");
        }

        if (!this.userTokenRepo.createTableIfNotExist()) {
            throw new AuthRuntimeException("user token table can not be created");
        }
    }


}
