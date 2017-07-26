package blog.service;

import blog.data.EIUserFavourRule;
import rest.Service;

import java.util.List;

public interface IUserFavourRuleService extends Service {

    List<EIUserFavourRule> list();

    EIUserFavourRule save(EIUserFavourRule userFavourRule);

    List<EIUserFavourRule> listNotDisabled();

    Boolean isFillRule(Long userId, Long ruleId, int limit);

    Boolean fullFillRule(Long userId, Long ruleId);
}
