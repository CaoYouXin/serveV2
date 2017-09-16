package diary.repository;

import diary.data.EIDiaryBook;
import orm.Query;
import orm.Repository;

import java.util.List;

public interface IDiaryBookRepo extends Repository<EIDiaryBook, Long> {

    List<EIDiaryBook> findAllByDiaryBookDisabledAndResourceLevelId(Boolean b, Long i);

    @Query(useValue = false)
    List<EIDiaryBook> queryAllInResourceLevel(String sql);
}
