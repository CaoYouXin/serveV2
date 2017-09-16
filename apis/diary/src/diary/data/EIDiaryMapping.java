package diary.data;

import beans.EntityBeanI;

import javax.persistence.*;

@Entity(name = "diary_mapping")
public interface EIDiaryMapping extends EntityBeanI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getDiaryMappingId();

    void setDiaryMappingId(Long diaryMappingId);

    @Column(name = "book_id", nullable = false)
    Long getDiaryBookId();

    void setDiaryBookId(Long diaryBookId);

    @Column(name = "page_id", nullable = false)
    Long getDiaryPageId();

    void setDiaryPageId(Long diaryPageId);

    @Column(name = "diabled", nullable = false)
    Boolean isDiaryMappingDisabled();

    void setDiaryMappingDisabled(Boolean diaryMappingDisabled);

}
