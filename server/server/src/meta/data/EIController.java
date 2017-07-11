package meta.data;

import beans.EntityBeanI;

import javax.persistence.*;

@Entity(name = "controller")
public interface EIController extends EntityBeanI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getControllerId();

    void setControllerId(Long id);

    @Column(name = "name", nullable = false)
    String getControllerName();

    void setControllerName(String name);

    @Column(name = "className", nullable = false)
    String getControllerClassName();

    void setControllerClassName(String className);

    @Column(name = "disabled", nullable = false)
    Boolean isControllerDisabled();

    void setControllerDisabled(Boolean disabled);

}
