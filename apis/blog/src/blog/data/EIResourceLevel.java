package blog.data;

import beans.EntityBeanI;

import javax.persistence.*;

@Entity(name = "blog_resource_level")
public interface EIResourceLevel extends EntityBeanI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getResourceLevelId();

    void setResourceLevelId(Long resourceLevelId);

    @Column(name = "name", nullable = false)
    String getResourceLevelName();

    void setResourceLevelName(String resourceName);

    @Column(name = "expMsg", nullable = false)
    String getResourceLevelExpMsg();

    void setResourceLevelExpMsg(String resourceLevelExpMsg);

}
