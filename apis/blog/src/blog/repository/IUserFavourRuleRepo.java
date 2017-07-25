package blog.repository;

import blog.data.EIUserFavourRule;
import orm.Repository;

import java.util.List;

public interface IUserFavourRuleRepo extends Repository<EIUserFavourRule, Long> {

    List<EIUserFavourRule> findAllByUserFavourRuleDisabled(Boolean disabled);

}
