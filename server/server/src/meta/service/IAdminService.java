package meta.service;

import meta.service.exp.AdminSettingException;
import rest.Service;

public interface IAdminService extends Service {

    Boolean check();

    Boolean setting(String oldUserName, String username, String oldPassword, String password) throws AdminSettingException;

}
