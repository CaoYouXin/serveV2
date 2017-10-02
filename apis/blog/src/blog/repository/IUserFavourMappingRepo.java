package blog.repository;

import blog.data.EIResourceLevel;
import blog.data.EIUserFavourMapping;
import blog.view.EIUserFavourMappingDetail;
import orm.Query;
import orm.Repository;

import java.util.List;

public interface IUserFavourMappingRepo extends Repository<EIUserFavourMapping, Long> {

    @Query(value = "select b from EIUserFavourMapping a, EIResourceLevel b where a.ResourceLevelId = b.ResourceLevelId and a.UserFavourThreshold <= :0", types = {EIUserFavourMapping.class})
    List<EIResourceLevel> queryByThreshold(Integer threshold);

    @Query(value = "select a, b from EIUserFavourMapping a, EIResourceLevel b where a.ResourceLevelId = b.ResourceLevelId", types = {EIUserFavourMapping.class, EIResourceLevel.class})
    List<EIUserFavourMappingDetail> queryAll();

    EIUserFavourMapping findByResourceLevelId(Long ResourceLevelId);

}
