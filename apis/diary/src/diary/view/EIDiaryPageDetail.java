package diary.view;

import diary.data.EIDiaryMilestone;
import diary.data.EIDiaryPage;
import diary.data.EIDiaryPhoto;

import java.util.List;

public interface EIDiaryPageDetail extends EIDiaryPage {

    List<EIDiaryMilestone> getDiaryMilestones();

    void setDiaryMilestones(List<EIDiaryMilestone> diaryMilestoneList);

    List<EIDiaryPhoto> getDiaryPhotoes();

    void setDiaryPhotoes(List<EIDiaryPhoto> diaryPhotoList);

}
