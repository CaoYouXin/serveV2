package blog.repository;

import blog.data.EIResourceLevelMapping;
import orm.Repository;

import java.util.List;

public interface IResourceLevelMappingRepo extends Repository<EIResourceLevelMapping, Long> {

    List<EIResourceLevelMapping> findAllByResourceLevelMappingDisabled(Boolean resourceLevelMappingDisabled);

    Boolean softRemoveByResourceLevelMappingIdAtResourceLevelMappingDisabled(Long resourceLevelMappingId);

}
