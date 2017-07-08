package meta.data;

import beans.EntityBeanI;

import javax.persistence.*;

@Entity(name = "config")
public interface EIConfig extends EntityBeanI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getConfigId();

    void setConfigId(Long a);

    @Column(name = "key", nullable = false)
    String getConfigKey();

    void setConfigKey(String key);

    @Column(name = "value", nullable = false)
    String getConfigValue();

    void setConfigValue(String value);

}
