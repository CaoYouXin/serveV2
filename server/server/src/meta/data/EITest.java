package meta.data;

import javax.persistence.*;

@Entity(name = "version")
public interface EITest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getVersionId();

    void setVersionId(Long a);

}
