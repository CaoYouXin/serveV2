package diary.repository;

import diary.data.EIDiaryPhoto;
import orm.Repository;

import java.util.List;

public interface IDiaryPhotoRepo extends Repository<EIDiaryPhoto, Long> {

    List<EIDiaryPhoto> findAllByDiaryPageId(Long pageId);

}
