package meta.view;

import beans.EntityBeanI;

public interface EIAdminVerify extends EntityBeanI {

    String getUserName();

    void setUserName(String userName);

    String getPassword();

    void setPassword(String password);

}
