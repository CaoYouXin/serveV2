package orm;

import beans.EntityBeanI;

import javax.persistence.*;

@Entity(name = "test_table")
public interface ITestEntity extends EntityBeanI {

    void setTestId(Long a);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getTestId();

    void setTestValue(String a);
    @Column(name = "value")
    String getTestValue();

}
