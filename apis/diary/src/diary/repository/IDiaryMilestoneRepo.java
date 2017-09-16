package diary.repository;

import diary.data.EIDiaryMilestone;
import orm.Repository;

import java.util.List;

public interface IDiaryMilestoneRepo extends Repository<EIDiaryMilestone, Long> {

    List<EIDiaryMilestone> findAllByDiaryPageId(Long pageId);

}
