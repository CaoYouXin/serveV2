package blog.view;

import beans.EntityBeanI;

import javax.persistence.Column;

public interface EICount extends EntityBeanI {

    @Column
    Integer getCount();

    void setCount(Integer count);

}
