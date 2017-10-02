package blog.repository;

import blog.data.EIResourceLevel;
import blog.data.EIUserFavour;
import blog.data.EIUserFavourMapping;
import orm.Query;
import orm.Repository;

import java.util.List;

public interface IResourceLevelRepo extends Repository<EIResourceLevel, Long> {

    @Query(value = "select b from EIUserFavourMapping a, EIResourceLevel b, EIUserFavour c where c.UserId = :0 and a.ResourceLevelId = b.ResourceLevelId and a.UserFavourThreshold <= c.UserFavourValue", types = {EIUserFavourMapping.class, EIUserFavour.class})
    List<EIResourceLevel> queryByUserId(Long userId);

}
