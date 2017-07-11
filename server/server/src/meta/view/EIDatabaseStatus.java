package meta.view;

import beans.EntityBeanI;

public interface EIDatabaseStatus extends EntityBeanI {

    void setConfigs(String[] list);

    String[] getConfigs();

    void setActiveConfig(String str);

    String getActiveConfig();

    void setActiveDatasource(Boolean b);

    Boolean isActiveDatasource();

}
