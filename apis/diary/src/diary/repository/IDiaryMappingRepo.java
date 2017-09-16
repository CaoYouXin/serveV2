package diary.repository;

import diary.data.EIDiaryMapping;
import orm.Repository;

public interface IDiaryMappingRepo extends Repository<EIDiaryMapping, Long> {

    EIDiaryMapping findByDiaryBookIdAndDiaryPageId(Long diaryBookId, Long diaryPageId);

}
