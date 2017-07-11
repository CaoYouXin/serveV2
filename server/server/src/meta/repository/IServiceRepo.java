package meta.repository;

import meta.data.EIService;
import orm.Repository;

import java.util.List;

public interface IServiceRepo extends Repository<EIService, Long> {

    EIService findByServiceIntfClassName(String className);

    List<EIService> findAllByServiceDisabled(Boolean disabled);

}
