package blog.service.impl;

import beans.BeanManager;
import blog.data.EIUserFavour;
import blog.repository.IUserFavourRepo;
import blog.service.IUserFavourService;
import blog.service.exp.TableNotCreateException;
import blog.service.exp.UserFavourException;
import blog.view.EIUserFavourDetail;

import java.util.List;

public class UserFavourServiceImpl implements IUserFavourService {

    private IUserFavourRepo userFavourRepo = BeanManager.getInstance().getRepository(IUserFavourRepo.class);

    @Override
    public void before() {
        if (!this.userFavourRepo.createTableIfNotExist()) {
            throw new TableNotCreateException("user favour");
        }
    }

    @Override
    public List<EIUserFavourDetail> list() {
        return this.userFavourRepo.queryAll();
    }

    @Override
    public EIUserFavour save(EIUserFavour userFavour) throws UserFavourException {
        EIUserFavour byUserId = this.userFavourRepo.findByUserId(userFavour.getUserId());
        if (null == byUserId) {
            byUserId = userFavour;
        } else {
            byUserId.setUserFavourValue(userFavour.getUserFavourValue());
        }

        if (!this.userFavourRepo.save(byUserId)) {
            throw new UserFavourException("无法保存好感度.");
        }

        return byUserId;
    }

    @Override
    public Boolean increaseFavour(Long userId, int inc) {
        EIUserFavour eiUserFavour = this.userFavourRepo.findByUserId(userId);
        if (null == eiUserFavour) {
            eiUserFavour = BeanManager.getInstance().createBean(EIUserFavour.class);
            eiUserFavour.setUserId(userId);
            eiUserFavour.setUserFavourValue(0);
        }
        eiUserFavour.setUserFavourValue(eiUserFavour.getUserFavourValue() + inc);

        return this.userFavourRepo.save(eiUserFavour);
    }
}
