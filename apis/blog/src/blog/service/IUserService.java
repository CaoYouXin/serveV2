package blog.service;

import blog.data.EIUser;
import blog.service.exp.UserException;
import blog.view.EILoginUser;
import blog.view.EIRegisterUser;
import rest.Service;

import java.util.List;

public interface IUserService extends Service {

    Boolean register(EIRegisterUser registerUser) throws UserException;

    EILoginUser login(EIUser user) throws UserException;

    List<EIUser> listUsers() throws UserException;

    Boolean disable(Long id, Boolean disabled) throws UserException;

}
