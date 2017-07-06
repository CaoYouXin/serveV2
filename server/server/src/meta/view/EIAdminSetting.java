package meta.view;

import beans.EntityBeanI;

public interface EIAdminSetting extends EntityBeanI {

    void setOldUserName(String oldUserName);
    String getOldUserName();

    void setUserName(String userName);
    String getUserName();

    void setOldPassword(String oldPassword);
    String getOldPassword();

    void setPassword(String password);
    String getPassword();

}
