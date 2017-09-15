package diary.data;

import javax.persistence.*;

@Entity(name = "diary_photo")
public interface EIDiaryPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getDiaryPhotoId();

    void setDiaryPhotoId(Long diaryPhotoId);

    @Column(name = "page_id", nullable = false)
    Long getDiaryPageId();

    void setDiaryPageId(Long diaryPageId);

    @Column(name = "url", nullable = false, length = 1024)
    String getDiaryPhotoUrl();

    void setDiaryPhotoUrl(String diaryPhotoUrl);

    @Column(name = "disabled", nullable = false)
    Boolean isDiaryPhotoDisabled();

    void setDiaryPhotoDisabled(Boolean diaryPhotoDisabled);

}
