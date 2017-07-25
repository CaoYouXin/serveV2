package blog.data;

import beans.EntityBeanI;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public interface EIUserFavourRuleRec extends EntityBeanI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getUserFavourRuleRecId();

    void setUserFavourRuleRecId(Long userFavourRuleRecId);

    @Column(name = "user_id", nullable = false)
    Long getUserId();

    void setUserId(Long userId);

    @Column(name = "rule_id", nullable = false)
    Long getUserFavourRuleId();

    void setUserFavourRuleId(Long userFavourRuleId);

    @Column(name = "count", nullable = false)
    Integer getRecCount();

    void setRecCount(Integer recCount);

}
