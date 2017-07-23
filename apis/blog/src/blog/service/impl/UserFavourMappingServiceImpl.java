package blog.service.impl;

import beans.BeanManager;
import blog.data.EIUserFavourMapping;
import blog.repository.IUserFavourMappingRepo;
import blog.service.IUserFavourMappingService;
import blog.service.base.BaseService;
import orm.Repository;

public class UserFavourMappingServiceImpl extends BaseService<EIUserFavourMapping, Long> implements IUserFavourMappingService {

    private IUserFavourMappingRepo userFavourMappingRepo = BeanManager.getInstance().getRepository(IUserFavourMappingRepo.class);

    @Override
    protected Repository<EIUserFavourMapping, Long> getRepository() {
        return this.userFavourMappingRepo;
    }

    @Override
    protected String getName() {
        return "user_favour - resource_level mapping";
    }
}
