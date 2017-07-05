package meta.data;

import javax.persistence.*;

@Entity(name = "version")
public interface EITest {

    void setVersionId(Long a);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getVersionId();

}
