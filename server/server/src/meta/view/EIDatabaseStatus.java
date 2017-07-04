package meta.view;

import beans.EntityBeanI;

public interface EIDatabaseStatus extends EntityBeanI {

    void setHasConfigs(Boolean b);
    Boolean isHasConfigs();

    void setHasActiveConfig(Boolean b);
    Boolean isHasActiveConfig();

    void setActiveDatasource(Boolean b);
    Boolean isActiveDatasource();

}
