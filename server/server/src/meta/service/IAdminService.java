package meta.service;

import meta.service.exp.AdminSettingException;
import meta.service.exp.AdminVerifyException;
import rest.Service;

public interface IAdminService extends Service {

    Boolean check();

    Boolean setting(String oldUserName, String username, String oldPassword, String password) throws AdminSettingException;

    String verify(String userName, String password) throws AdminVerifyException;

}
