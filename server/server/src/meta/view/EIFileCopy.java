package meta.view;

import beans.EntityBeanI;

import java.util.List;

public interface EIFileCopy extends EntityBeanI {

    List<String> getSrc();

    void setSrc(List<String> src);

    String getDst();

    void setDst(String dst);

}
