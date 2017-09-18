package diary.repository;

import diary.data.EIDiaryMapping;
import orm.Repository;

import java.util.List;

public interface IDiaryMappingRepo extends Repository<EIDiaryMapping, Long> {

    EIDiaryMapping findByDiaryBookIdAndDiaryPageId(Long diaryBookId, Long diaryPageId);

    List<EIDiaryMapping> findAllByDiaryBookIdAndDiaryMappingDisabled(Long diaryBookId, Boolean disabled);
}
