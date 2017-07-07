package meta.data;

import beans.EntityBeanI;

import javax.persistence.*;

@Entity(name = "config")
public interface EIConfig extends EntityBeanI {

    void setConfigId(Long a);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getConfigId();

    void setConfigKey(String key);
    @Column(name = "key", nullable = false)
    String getConfigKey();

    void setConfigValue(String value);
    @Column(name = "value", nullable = false)
    String getConfigValue();

}
