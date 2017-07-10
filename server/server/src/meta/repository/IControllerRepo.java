package meta.repository;

import meta.data.EIController;
import orm.Repository;

public interface IControllerRepo extends Repository<EIController, Long> {

    EIController findByControllerName(String name);

}
