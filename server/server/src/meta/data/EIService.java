package meta.data;

import beans.EntityBeanI;

import javax.persistence.*;

@Entity(name = "service")
public interface EIService extends EntityBeanI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getServiceId();

    void setServiceId(Long id);

    @Column(name = "intfClassName", nullable = false)
    String getServiceIntfClassName();

    void setServiceIntfClassName(String className);

    @Column(name = "implClassName", nullable = false)
    String getServiceImplClassName();

    void setServiceImplClassName(String className);

    @Column(name = "disabled", nullable = false)
    Boolean isServiceDisabled();

    void setServiceDisabled(Boolean disabled);

}
