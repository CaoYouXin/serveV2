package diary.service;

import diary.data.EIDiaryBook;
import diary.data.EIDiaryMilestone;
import diary.data.EIDiaryPage;
import diary.data.EIDiaryPhoto;
import diary.view.EIDiaryPageDetail;
import rest.Service;

import java.util.List;

public interface IDiaryService extends Service {

    EIDiaryBook saveBook(EIDiaryBook diaryBook);

    EIDiaryPage savePage(EIDiaryPage diaryPage);

    EIDiaryMilestone saveMilestone(EIDiaryMilestone diaryMilestone);

    EIDiaryPhoto savePhoto(EIDiaryPhoto diaryPhoto);

    Boolean attachPage(Long bookId, Long pageId);

    Boolean releasePage(Long bookId, Long pageId);

    List<EIDiaryBook> listBooks();

    List<EIDiaryPage> listPages();

    List<EIDiaryMilestone> listMilestones(Long pageId);

    List<EIDiaryPhoto> listPhotos(Long pageId);

    List<EIDiaryBook> listBooks(Long userId);

    List<EIDiaryPageDetail> listSimplePages(Long bookId);

    EIDiaryPageDetail getDetailedPage(Long pageId);

}
