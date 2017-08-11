package blog.service.impl;

import beans.BeanManager;
import blog.data.EICaptcha;
import blog.data.EIUser;
import blog.data.EIUserToken;
import blog.repository.ICaptchaRepo;
import blog.repository.IUserRepo;
import blog.repository.IUserTokenRepo;
import blog.service.IUserService;
import blog.service.exp.TableNotCreateException;
import blog.service.exp.UserException;
import blog.view.EILoginUser;
import blog.view.EIRegisterUser;
import util.DateUtil;
import util.StringUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UserServiceImpl implements IUserService {

    private IUserRepo userRepo = BeanManager.getInstance().getRepository(IUserRepo.class);
    private IUserTokenRepo userTokenRepo = BeanManager.getInstance().getRepository(IUserTokenRepo.class);
    private ICaptchaRepo captchaRepo = BeanManager.getInstance().getRepository(ICaptchaRepo.class);

    @Override
    public void before() {
        if (!this.userRepo.createTableIfNotExist()) {
            throw new TableNotCreateException("user");
        }

        if (!this.userTokenRepo.createTableIfNotExist()) {
            throw new TableNotCreateException("user token");
        }

        if (!this.captchaRepo.createTableIfNotExist()) {
            throw new TableNotCreateException("captcha");
        }
    }

    @Override
    public Boolean register(EIRegisterUser registerUser) throws UserException {
        EICaptcha eiCaptcha = this.captchaRepo.queryByCaptchaTokenAndCaptchaDeadTime(registerUser.getImageCaptchaToken(), new Date());
        if (null == eiCaptcha) {
            throw new UserException("验证码已过期.");
        }

        if (!eiCaptcha.getCaptchaCode().equals(registerUser.getImageCaptcha())) {
            throw new UserException("验证码不正确.");
        }

        if (null == registerUser.getUserDisabled()) {
            registerUser.setUserDisabled(false);
        }

        if (!this.userRepo.save(registerUser)) {
            throw new UserException("无法创建新用户.");
        }

        return true;
    }

    @Override
    public EILoginUser login(EIUser user) throws UserException {
        EIUser eiUser = this.userRepo.findByUserName(user.getUserName());
        if (null == eiUser) {
            throw new UserException("没有该用户.");
        }

        if (!eiUser.getUserPassword().equals(user.getUserPassword())) {
            throw new UserException("密码不正确.");
        }

        EIUserToken eiUserToken = BeanManager.getInstance().createBean(EIUserToken.class);
        eiUserToken.setUserId(eiUser.getUserId());
        eiUserToken.setUserToken(StringUtil.getMD5(new Date().toString()));
        eiUserToken.setUserTokenDeadTime(DateUtil.calc(new Date(), Calendar.DAY_OF_YEAR, 3));

        if (!this.userTokenRepo.save(eiUserToken)) {
            throw new UserException("不能保存Token.");
        }

        EILoginUser eiLoginUser = BeanManager.getInstance().createBean(EILoginUser.class);
        eiLoginUser.copyFrom(eiUserToken);
        eiLoginUser.copyFrom(eiUser);
        eiLoginUser.setUserPassword(null);

        return eiLoginUser;
    }

    @Override
    public List<EIUser> listUsers() throws UserException {
        return this.userRepo.findAll();
    }

    @Override
    public Boolean disable(Long id, Boolean disabled) throws UserException {
        EIUser eiUser = this.userRepo.find(id);
        if (null == eiUser) {
            throw new UserException("没有找到该用户.");
        }

        eiUser.setUserDisabled(disabled);

        if (!this.userRepo.save(eiUser)) {
            throw new UserException("不能保存用户.");
        }

        return disabled;
    }
}
