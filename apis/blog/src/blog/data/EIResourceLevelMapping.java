package blog.data;

import javax.persistence.*;

@Entity(name = "blog_resource_level_mapping")
public interface EIResourceLevelMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getResourceLevelMappingId();

    void setResourceLevelMappingId(Long resourceLevelMappingId);

    @Column(name = "level_id")
    Long getResourceLevelId();

    void setResourceLevelId(Long resourceLevelId);

    @Column(name = "url_prefix", length = 1024, unique = true)
    String getResourceUrlPrefix();

    void setResourceUrlPrefix(String resourceUrlPrefix);

    @Column(name = "disabled")
    Boolean getResourceLevelMappingDisabled();

    void setResourceLevelMappingDisabled(Boolean resourceLevelMappingDisabled);

}
