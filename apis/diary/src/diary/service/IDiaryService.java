package diary.service;

import diary.data.*;
import diary.view.EIDiaryBookDetail;
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

    List<EIDiaryBookDetail> listBooks();

    List<EIDiaryPage> listPages();

    List<EIDiaryMapping> listPages(Long bookId);

    List<EIDiaryMilestone> listMilestones(Long pageId);

    List<EIDiaryPhoto> listPhotos(Long pageId);

    List<EIDiaryBook> listBooks(Long userId);

    List<EIDiaryPageDetail> listSimplePages(Long userId, Long bookId);

    EIDiaryPageDetail getDetailedPage(Long pageId);

}
