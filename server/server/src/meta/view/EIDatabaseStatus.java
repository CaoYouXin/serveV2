package meta.view;

import beans.EntityBeanI;

public interface EIDatabaseStatus extends EntityBeanI {

    String[] getConfigs();

    void setConfigs(String[] list);

    String getActiveConfig();

    void setActiveConfig(String str);

    void setActiveDatasource(Boolean b);

    Boolean isActiveDatasource();

}
