package blog.repository;

import blog.data.EIResourceLevel;
import blog.data.EIResourceLevelMapping;
import blog.view.EIResourceLevelMappingDetail;
import orm.Query;
import orm.Repository;

import java.util.List;

public interface IResourceLevelMappingRepo extends Repository<EIResourceLevelMapping, Long> {

    @Query(value = "select a.ResourceLevelId, a.ResourceLevelName, a.ResourceLevelExpMsg, b.ResourceUrlPrefix, b.ResourceLevelMappingId, b.ResourceLevelMappingDisabled from EIResourceLevelMapping b, EIResourceLevel a where a.ResourceLevelId = b.ResourceLevelId", types = {EIResourceLevelMapping.class, EIResourceLevel.class})
    List<EIResourceLevelMappingDetail> queryAll();

    List<EIResourceLevelMapping> findAllByResourceLevelMappingDisabled(Boolean resourceLevelMappingDisabled);

}
