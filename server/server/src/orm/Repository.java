package orm;

import beans.ChangeOriginI;

import java.util.List;

public interface Repository<TABLE, ID> extends ChangeOriginI {

    TABLE find(ID id);
    // findByXxx
    // findUnRemovedAtXxx

    List<TABLE> findAll();
    // findAllByXxx
    // findAllUnRemovedAtXxx

    Boolean remove(ID id);
    // removeByXxx
    // softRemoveByXxxAtYyy

    TABLE save(TABLE t);

    List<TABLE> saveAll(List<TABLE> listT);

    Boolean createTableIfNotExist();

}
