package blog.repository;

import blog.data.EIUserFavourRuleRec;
import orm.Repository;

public interface IUserFavourRuleRecRepo extends Repository<EIUserFavourRuleRec, Long> {

    EIUserFavourRuleRec findByUserIdAndUserFavourRuleId(Long userId, Long ruleId);
}
