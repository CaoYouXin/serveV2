package blog.data;

import beans.EntityBeanI;

import javax.persistence.*;

@Entity(name = "blog_favour_record")
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
