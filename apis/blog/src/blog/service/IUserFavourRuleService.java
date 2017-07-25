package blog.service;

import blog.data.EIUserFavourRule;
import blog.service.exp.UserFavourRuleException;
import rest.Service;

import java.util.List;

public interface IUserFavourRuleService extends Service {

    List<EIUserFavourRule> list();

    List<EIUserFavourRule> listNotDisabled();

    EIUserFavourRule save(EIUserFavourRule userFavourRule);

    Boolean disabled(Long id, Boolean disabled) throws UserFavourRuleException;

    Boolean isFillRule(Long userId, Long ruleId, int limit);

    Boolean fullFillRule(Long userId, Long ruleId);
}
