package meta.repository;

import meta.data.EIConfig;
import orm.Repository;

public interface IConfigRepo extends Repository<EIConfig, Long> {

    EIConfig findByConfigKey(String key);

}
