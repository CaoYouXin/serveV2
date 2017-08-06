package blog.service.impl;

import beans.BeanManager;
import blog.data.EIUserFavourMapping;
import blog.repository.IUserFavourMappingRepo;
import blog.service.IUserFavourMappingService;
import blog.service.base.BaseService;
import blog.service.exp.TableNotCreateException;
import blog.view.EIUserFavourMappingDetail;
import orm.Repository;

import java.util.List;

public class UserFavourMappingServiceImpl implements IUserFavourMappingService {

    private IUserFavourMappingRepo userFavourMappingRepo = BeanManager.getInstance().getRepository(IUserFavourMappingRepo.class);

    @Override
    public void before() {
        if (!this.userFavourMappingRepo.createTableIfNotExist()) {
            throw new TableNotCreateException("user favour mapping");
        }
    }

    @Override
    public List<EIUserFavourMappingDetail> list() {
        return this.userFavourMappingRepo.queryAll();
    }

    @Override
    public EIUserFavourMapping save(EIUserFavourMapping data) {
        EIUserFavourMapping byResourceLevelId = this.userFavourMappingRepo.findByResourceLevelId(data.getResourceLevelId());
        if (null == byResourceLevelId) {
            byResourceLevelId = data;
        } else {
            byResourceLevelId.setUserFavourThreshold(data.getUserFavourThreshold());
        }

        if (!this.userFavourMappingRepo.save(byResourceLevelId)) {
            throw new RuntimeException("can not save user favour mapping");
        }

        return byResourceLevelId;
    }
}
