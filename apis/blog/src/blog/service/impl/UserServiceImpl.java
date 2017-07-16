package blog.service.impl;

import beans.BeanManager;
import blog.data.EIUser;
import blog.repository.IUserRepo;
import blog.repository.IUserTokenRepo;
import blog.service.IUserService;
import blog.service.exp.UserException;
import blog.view.EILoginUser;
import blog.view.EIRegisterUser;

import java.util.List;

public class UserServiceImpl implements IUserService {

    private IUserRepo userRepo = BeanManager.getInstance().getRepository(IUserRepo.class);
    private IUserTokenRepo userTokenRepo = BeanManager.getInstance().getRepository(IUserTokenRepo.class);

    @Override
    public void before() {
        if (!this.userRepo.createTableIfNotExist()) {
            throw new RuntimeException("user table can not be created.");
        }

        if (!this.userTokenRepo.createTableIfNotExist()) {
            throw new RuntimeException("user token table can not be created.");
        }
    }

    @Override
    public Boolean register(EIRegisterUser registerUser) throws UserException {
        return null;
    }

    @Override
    public EILoginUser login(EIUser user) throws UserException {
        return null;
    }

    @Override
    public List<EIUser> listUsers() throws UserException {
        return null;
    }
}
