package orm;

import beans.EntityBeanI;

import javax.persistence.*;

@Entity(name = "test_table")
public interface ITestEntity extends EntityBeanI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getTestId();

    void setTestId(Long a);

    @Column(name = "value")
    String getTestValue();

    void setTestValue(String a);

}
