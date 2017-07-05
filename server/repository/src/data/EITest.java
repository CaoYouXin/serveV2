package data;

import beans.EntityBeanI;

import javax.persistence.*;

@Entity(name = "test")
public interface EITest extends EntityBeanI {

    void setTestId(Long a);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getTestId();

    void setTestString(String str);
    @Column(name = "test")
    String getTestString();

}
