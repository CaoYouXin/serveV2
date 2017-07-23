package meta.repository;

import meta.data.EIInterceptor;
import orm.Repository;

import java.util.List;

public interface IInterceptorRepo extends Repository<EIInterceptor, Long> {

    EIInterceptor findByInterceptorName(String interceptorName);

    List<EIInterceptor> findAllByInterceptorDisabled(Boolean disabled);

}
