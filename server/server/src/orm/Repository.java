package orm;

import beans.ChangeOriginI;

import java.util.List;

public interface Repository<TABLE, ID> extends ChangeOriginI {

    TABLE find(ID id);
    // findByXxx

    List<TABLE> findAll();
    // findAllByXxx

    Boolean remove(ID id);
    // removeByXxx
    // softRemoveByXxxAtYyy

    Boolean save(TABLE t);

    Boolean createTableIfNotExist();

}
