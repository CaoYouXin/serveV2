package diary.data;

import beans.EntityBeanI;

import javax.persistence.*;

@Entity(name = "diary_book")
public interface EIDiaryBook extends EntityBeanI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getDiaryBookId();

    void setDiaryBookId(Long diaryBookId);

    @Column(name = "title", unique = true, nullable = false)
    String getDiaryBookTitle();

    void setDiaryBookTitle(String diaryBookTitle);

    @Column(name = "page_count", nullable = false)
    Integer getDiaryBookPageCount();

    void setDiaryBookPageCount(Integer diaryBookPageCount);

    @Column(name = "resource_level_id")
    Long getResourceLevelId();

    void setResourceLevelId(Long resourceLevelId);

    @Column(name = "disabled", nullable = false)
    Boolean isDiaryBookDisabled();

    void setDiaryBookDisabled(Boolean diaryBookDisabled);

}
