package blog.repository;

import blog.data.EIResourceLevel;
import blog.data.EIUserFavourMapping;
import orm.Query;
import orm.Repository;

import java.util.List;

public interface IUserFavourMappingRepo extends Repository<EIUserFavourMapping, Long> {

    @Query(value = "select b from EIUserFavourMapping a, EIResourceLevel b where a.ResourceLevelId = b.ResourceLevelId and a.UserFavourThreshold <= $0", types = {EIUserFavourMapping.class})
    List<EIResourceLevel> queryByThreshold(Integer threshold);

}
