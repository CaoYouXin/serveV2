package blog.interceptor;

import beans.BeanManager;
import blog.data.EIUserFavourRule;
import blog.service.IUserFavourRuleService;
import blog.service.IUserFavourService;
import config.Configs;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import orm.DatasourceFactory;
import rest.Interceptor;
import rest.RestHelper;
import rest.WithMatcher;

import java.io.IOException;
import java.sql.Connection;
import java.util.*;
import java.util.regex.Pattern;

public class UserFavourInterceptor extends WithMatcher implements Interceptor {

    public static final String USER_FAVOUR_INTERCEPTOR_CONFIG_KEY = "USER_FAVOUR_INTERCEPTOR_CONFIG_KEY";
    private static final Logger logger = LogManager.getLogger(UserFavourInterceptor.class);
    private IUserFavourService userFavourService = BeanManager.getInstance().getService(IUserFavourService.class);
    private IUserFavourRuleService userFavourRuleService = BeanManager.getInstance().getService(IUserFavourRuleService.class);
    private List<EIUserFavourRule> userFavourRules;
    private Map<String, Pattern> patternCache;

    @Override
    public String name() {
        return "user favour interceptor";
    }

    @Override
    public String urlPattern() {
        return ".*";
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {

        Long userId = (Long) httpContext.getAttribute("infinitely-serve-user_id");
        if (null == userId) {
            return;
        }

        if (null == this.userFavourRules || !Configs.getConfigs(USER_FAVOUR_INTERCEPTOR_CONFIG_KEY, Boolean.class)) {
            this.userFavourRules = this.userFavourRuleService.listNotDisabled();
            HashMap<String, Pattern> newCache = new HashMap<>();
            for (EIUserFavourRule userFavourRule : this.userFavourRules) {
                String userFavourRulePattern = userFavourRule.getUserFavourRulePattern();
                if (this.patternCache.containsKey(userFavourRulePattern)) {
                    newCache.put(userFavourRulePattern, this.patternCache.get(userFavourRulePattern));
                }
            }
            this.patternCache = newCache;
            Configs.setConfigs(USER_FAVOUR_INTERCEPTOR_CONFIG_KEY, true);
        }

        String decodedUrl = RestHelper.getDecodedUrl(httpRequest);

        List<EIUserFavourRule> theMostMatchedRules = new ArrayList<>();
        for (EIUserFavourRule userFavourRule : this.userFavourRules) {
            String userFavourRulePattern = userFavourRule.getUserFavourRulePattern();
            Pattern pattern = this.patternCache.get(userFavourRulePattern);
            if (null == pattern) {
                pattern = Pattern.compile(userFavourRulePattern);
                this.patternCache.put(userFavourRulePattern, pattern);
            }

            if (pattern.matcher(decodedUrl).matches()) {
                theMostMatchedRules.add(userFavourRule);
            }
        }

        if (theMostMatchedRules.isEmpty()) {
            return;
        }

        theMostMatchedRules.sort(Comparator.comparingInt(EIUserFavourRule::getUserFavourRuleScore));

        for (EIUserFavourRule eiUserFavourRule : theMostMatchedRules) {
            DatasourceFactory.begin(Connection.TRANSACTION_SERIALIZABLE);
            if (!this.userFavourRuleService.isFillRule(userId, eiUserFavourRule.getUserFavourRuleId(), eiUserFavourRule.getUserFavourRuleLimit())) {
                DatasourceFactory.rollback();
                continue;
            }

            if (!this.userFavourService.increaseFavour(userId, eiUserFavourRule.getUserFavourRuleScore())) {
                logger.error(String.format("ID为【%s】的用户增加好感度未成功.", userId));
                DatasourceFactory.rollback();
                return;
            }

            if (!this.userFavourRuleService.fullFillRule(userId, eiUserFavourRule.getUserFavourRuleId())) {
                logger.error(String.format("ID为【%s】的用户增加好感度记录未成功.", userId));
                DatasourceFactory.rollback();
            } else {
                DatasourceFactory.commit();
            }
            return;
        }

    }
}
