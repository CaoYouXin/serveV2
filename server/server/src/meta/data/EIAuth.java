package meta.data;

import beans.EntityBeanI;

import javax.persistence.*;

@Entity(name = "auth")
public interface EIAuth extends EntityBeanI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getAuthId();

    void setAuthId(Long authId);

    @Column(name = "type", nullable = false)
    Integer getAuthType();

    void setAuthType(Integer authType);

    @Column(name = "className", nullable = false)
    String getAuthClassName();

    void setAuthClassName(String className);

}
