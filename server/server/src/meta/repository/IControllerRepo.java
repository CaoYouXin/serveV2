package meta.repository;

import meta.data.EIController;
import orm.Repository;

import java.util.List;

public interface IControllerRepo extends Repository<EIController, Long> {

    EIController findByControllerName(String name);

    List<EIController> findAllByControllerDisabled(Boolean disabled);

}
