package orm;

import java.util.List;

public interface ITestRepo2 extends ITestRepo {

    @Query(value = "select a.TestId TestId from ITestEntity a", types = {ITestEntity.class})
    List<ITestEntity2> querySth2();

}
