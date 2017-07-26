package blog.data;

import beans.EntityBeanI;

import javax.persistence.*;

@Entity(name = "blog_resource_level_mapping")
public interface EIResourceLevelMapping extends EntityBeanI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getResourceLevelMappingId();

    void setResourceLevelMappingId(Long resourceLevelMappingId);

    @Column(name = "level_id", nullable = false)
    Long getResourceLevelId();

    void setResourceLevelId(Long resourceLevelId);

    @Column(name = "url_prefix", length = 1024, unique = true, nullable = false)
    String getResourceUrlPrefix();

    void setResourceUrlPrefix(String resourceUrlPrefix);

    @Column(name = "disabled", nullable = false)
    Boolean getResourceLevelMappingDisabled();

    void setResourceLevelMappingDisabled(Boolean resourceLevelMappingDisabled);

}
