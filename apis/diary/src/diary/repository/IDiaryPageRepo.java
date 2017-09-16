package diary.repository;

import diary.data.EIDiaryMapping;
import diary.data.EIDiaryMilestone;
import diary.data.EIDiaryPage;
import diary.data.EIDiaryPhoto;
import diary.view.EIDiaryPageDraft;
import orm.Query;
import orm.Repository;

import java.util.List;

public interface IDiaryPageRepo extends Repository<EIDiaryPage, Long> {

    @Query(value = "Select a, b, c From EIDiaryPage a left join EIDiaryPhoto b on a.DiaryPageId = b.DiaryPageId left join EIDiaryMilestone c on a.DiaryPageId = c.DiaryPageId Where a.DiaryPageDisabled = 0 and b.DiaryPhotoDisabled = 0 and c.DiaryMilestoneDisabled = 0", types = {EIDiaryPage.class, EIDiaryPhoto.class, EIDiaryMilestone.class})
    List<EIDiaryPageDraft> query(Long pageId);

    @Query(value = "Select a.DiaryPageId, a.DiaryPageType, a.DiaryPageTitle, a.DiaryPageStartDate, a.DiaryPageEndDate, a.DiaryPageRelated, b, c From EIDiaryPage a left join EIDiaryMilestone c on a.DiaryPageId = c.DiaryPageId, EIDiaryMapping d Where d.DiaryBookId = $0 and a.DiaryPageId = d.DiaryPageId and a.DiaryPageDisabled = 0 and c.DiaryMilestoneDisabled = 0", types = {EIDiaryPage.class, EIDiaryMilestone.class, EIDiaryMapping.class})
    List<EIDiaryPageDraft> queryAllSimple(Long bookId);

    @Query(value = "Select a From EIDiaryPage a, EIDiaryMapping b Where b.DiaryBookId = $0 and a.DiaryPageId = b.DiaryPageId", types = {EIDiaryPage.class, EIDiaryMapping.class})
    List<EIDiaryPage> queryAll(Long bookId);

}
