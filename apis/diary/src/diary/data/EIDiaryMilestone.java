package diary.data;

import beans.EntityBeanI;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "diary_milestone")
public interface EIDiaryMilestone extends EntityBeanI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getDiaryMilestoneId();

    void setDiaryMilestoneId(Long diaryMilestoneId);

    @Column(name = "page_id", nullable = false)
    Long getDiaryPageId();

    void setDiaryPageId(Long diaryPageId);

    @Column(name = "title", nullable = false, unique = true)
    String getDiaryMilestoneTitle();

    void setDiaryMilestoneTitle(String diaryMilestoneTitle);

    @Column(name = "date", nullable = false)
    Date getDiaryMilestoneDate();

    void setDiaryMilestoneDate(Date diaryMilestoneDate);

    @Column(name = "disabled", nullable = false)
    Boolean isDiaryMilestoneDisabled();

    void setDiaryMilestoneDisabled(Boolean diaryMilestoneDisabled);

}
