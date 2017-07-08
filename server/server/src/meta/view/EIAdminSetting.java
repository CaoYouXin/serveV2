package meta.view;

import beans.EntityBeanI;

public interface EIAdminSetting extends EntityBeanI {

    String getOldUserName();

    void setOldUserName(String oldUserName);

    String getUserName();

    void setUserName(String userName);

    String getOldPassword();

    void setOldPassword(String oldPassword);

    String getPassword();

    void setPassword(String password);

}
