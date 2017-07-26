package blog.data;

import beans.EntityBeanI;

import javax.persistence.*;

@Entity(name = "blog_user_favour_mapping")
public interface EIUserFavourMapping extends EntityBeanI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getUserFavourMappingId();

    void setUserFavourMappingId(Long userFavourMappingId);

    @Column(name = "level_id", nullable = false)
    Long getResourceLevelId();

    void setResourceLevelId(Long resourceLevelId);

    @Column(name = "threshold", nullable = false)
    Integer getUserFavourThreshold();

    void setUserFavourThreshold(Integer userFavourThreshold);

}
