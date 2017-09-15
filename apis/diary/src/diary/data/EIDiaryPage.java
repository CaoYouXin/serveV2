package diary.data;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "diary_page")
public interface EIDiaryPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getDiaryPageId();

    void setDiaryPageId(Long diaryPageId);

    @Column(name = "type", nullable = false)
    String getDiaryPageType();

    void setDiaryPageType(String diaryPageType);

    @Column(name = "title", nullable = false, unique = true)
    String getDiaryPageTitle();

    void setDiaryPageTitle(String diaryPathTitle);

    @Column(name = "start_date", nullable = false)
    Date getDiaryPageStartDate();

    void setDiaryPageStartDate(Date diaryPageStartDate);

    @Column(name = "end_date", nullable = false)
    Date getDiaryPageEndDate();

    void setDiaryPageEndDate(Date diaryPageEndDate);

    @Column(name = "content", nullable = false, length = 102400)
    String getDiaryPageContent();

    void setDiaryPageContent(String diaryPageContent);

    @Column(name = "location", nullable = false)
    String getDiaryPageLocation();

    void setDiaryPageLocation(String diaryPageLocation);

    @Column(name = "related", nullable = false)
    String getDiaryPageRelated();

    void setDiaryPageRelated(String diaryPageRelated);

    @Column(name = "disabled", nullable = false)
    Boolean isDiaryPageDisabled();

    void setDiaryPageDisabled(Boolean diaryPageDisabled);

}
