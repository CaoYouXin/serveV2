package blog.service.impl;

import beans.BeanManager;
import blog.repository.IUserFavourRepo;
import blog.service.IUserFavourService;
import blog.view.EIUserFavourDetail;

import java.util.List;

public class UserFavourServiceImpl implements IUserFavourService {

    private IUserFavourRepo userFavourRepo = BeanManager.getInstance().getRepository(IUserFavourRepo.class);

    @Override
    public List<EIUserFavourDetail> list() {
        return this.userFavourRepo.queryAll();
    }
}
