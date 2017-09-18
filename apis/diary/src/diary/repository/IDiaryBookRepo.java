package diary.repository;

import blog.data.EIResourceLevel;
import diary.data.EIDiaryBook;
import diary.view.EIDiaryBookDetail;
import orm.Query;
import orm.Repository;

import java.util.List;

public interface IDiaryBookRepo extends Repository<EIDiaryBook, Long> {

    List<EIDiaryBook> findAllByDiaryBookDisabledAndResourceLevelId(Boolean b, Long i);

    @Query(useValue = false)
    List<EIDiaryBook> queryAllInResourceLevel(String sql);

    @Query(value = "Select a, b From EIDiaryBook a left join EIResourceLevel b on a.ResourceLevelId = b.ResourceLevelId", types = {EIDiaryBook.class, EIResourceLevel.class})
    List<EIDiaryBookDetail> queryAll();
}
