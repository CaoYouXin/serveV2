package blog.service.impl;

import beans.BeanManager;
import blog.data.EIUserFavourRule;
import blog.data.EIUserFavourRuleRec;
import blog.interceptor.UserFavourInterceptor;
import blog.repository.IUserFavourRuleRecRepo;
import blog.repository.IUserFavourRuleRepo;
import blog.service.IUserFavourRuleService;
import blog.service.base.BaseService;
import config.Configs;
import orm.Repository;

import java.util.List;

public class UserFavourRuleServiceImpl extends BaseService<EIUserFavourRule, Long> implements IUserFavourRuleService {

    private IUserFavourRuleRepo userFavourRuleRepo = BeanManager.getInstance().getRepository(IUserFavourRuleRepo.class);
    private IUserFavourRuleRecRepo userFavourRuleRecRepo = BeanManager.getInstance().getRepository(IUserFavourRuleRecRepo.class);

    @Override
    public Boolean isFillRule(Long userId, Long ruleId, int limit) {
        if (limit <= 0) {
            return false;
        }

        EIUserFavourRuleRec byUserIdAndUserFavourRuleId = this.userFavourRuleRecRepo.findByUserIdAndUserFavourRuleId(userId, ruleId);

        return null == byUserIdAndUserFavourRuleId || byUserIdAndUserFavourRuleId.getRecCount() < limit;
    }

    @Override
    public Boolean fullFillRule(Long userId, Long ruleId) {
        EIUserFavourRuleRec byUserIdAndUserFavourRuleId = this.userFavourRuleRecRepo.findByUserIdAndUserFavourRuleId(userId, ruleId);
        if (null == byUserIdAndUserFavourRuleId) {
            byUserIdAndUserFavourRuleId = BeanManager.getInstance().createBean(EIUserFavourRuleRec.class);
            byUserIdAndUserFavourRuleId.setUserId(userId);
            byUserIdAndUserFavourRuleId.setUserFavourRuleId(ruleId);
            byUserIdAndUserFavourRuleId.setRecCount(0);
        }
        byUserIdAndUserFavourRuleId.setRecCount(byUserIdAndUserFavourRuleId.getRecCount() + 1);

        return this.userFavourRuleRecRepo.save(byUserIdAndUserFavourRuleId);
    }

    @Override
    protected Repository<EIUserFavourRule, Long> getRepository() {
        return this.userFavourRuleRepo;
    }

    @Override
    protected String getName() {
        return "user favour rule";
    }

    @Override
    public List<EIUserFavourRule> listNotDisabled() {
        return this.userFavourRuleRepo.findAllByUserFavourRuleDisabled(false);
    }

    @Override
    public EIUserFavourRule save(EIUserFavourRule data) {
        if (null == data.getUserFavourRuleDisabled()) {
            data.setUserFavourRuleDisabled(false);
        }

        EIUserFavourRule save = super.save(data);
        Configs.setConfigs(UserFavourInterceptor.USER_FAVOUR_INTERCEPTOR_CONFIG_KEY, false);
        return save;
    }
}
