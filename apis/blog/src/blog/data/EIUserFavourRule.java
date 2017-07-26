package blog.data;

import beans.EntityBeanI;

import javax.persistence.*;

@Entity(name = "blog_favour_rule")
public interface EIUserFavourRule extends EntityBeanI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getUserFavourRuleId();

    void setUserFavourRuleId(Long userFavourRuleId);

    @Column(name = "pattern", nullable = false)
    String getUserFavourRulePattern();

    void setUserFavourRulePattern(String userFavourRulePattern);

    @Column(name = "score", nullable = false)
    Integer getUserFavourRuleScore();

    void setUserFavourRuleScore(Integer userFavourRuleScore);

    @Column(name = "limit", nullable = false)
    Integer getUserFavourRuleLimit();

    void setUserFavourRuleLimit(Integer userFavourRuleLimit);

    @Column(name = "disabled", nullable = false)
    Boolean getUserFavourRuleDisabled();

    void setUserFavourRuleDisabled(Boolean userFavourRuleDisabled);

}
