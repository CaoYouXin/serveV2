package meta.view;

import beans.EntityBeanI;

public interface EIFileInfo extends EntityBeanI {

    Boolean isDir();

    void setDir(Boolean b);

    String getName();

    void setName(String name);

}
