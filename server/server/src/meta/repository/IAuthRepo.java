package meta.repository;

import meta.data.EIAuth;
import orm.Repository;

public interface IAuthRepo extends Repository<EIAuth, Long> {

    EIAuth findByAuthType(Integer authType);
}
