package diary.service;

import diary.data.EIDiaryBook;
import diary.data.EIDiaryMilestone;
import diary.data.EIDiaryPage;
import diary.data.EIDiaryPhoto;
import diary.view.EIDiaryPageDetail;
import rest.Service;

import java.util.List;

public interface IDiaryService extends Service {

    Boolean saveBook(EIDiaryBook diaryBook);

    Boolean savePage(EIDiaryPage diaryPage);

    Boolean saveMilestone(EIDiaryMilestone diaryMilestone);

    Boolean savePhoto(EIDiaryPhoto diaryPhoto);

    Boolean deleteBook(Long id);

    Boolean deletePage(Long id);

    Boolean deleteMilestone(Long id);

    Boolean deletePhoto(Long id);

    Boolean attachPage(Long bookId, Long pageId);

    Boolean releasePage(Long bookId, Long pageId);

    List<EIDiaryBook> listBooks();

    List<EIDiaryPage> listPages(Long bookId);

    List<EIDiaryMilestone> listMilestones(Long pageId);

    List<EIDiaryPhoto> listPhotos(Long pageId);

    List<EIDiaryBook> listBooks(Long userId);

    List<EIDiaryPageDetail> listSimplePages(Long bookId);

    EIDiaryPageDetail getDetailedPage(Long pageId);

}
