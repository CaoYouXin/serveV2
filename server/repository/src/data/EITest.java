package data;

import beans.EntityBeanI;

import javax.persistence.*;

@Entity(name = "test")
public interface EITest extends EntityBeanI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getTestId();

    void setTestId(Long a);

    @Column(name = "test")
    String getTestString();

    void setTestString(String str);

}
