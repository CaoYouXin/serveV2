package orm;

import java.util.List;

public interface ITestRepo extends Repository<ITestEntity, Long> {

    ITestEntity findByTestValueOrTestId(String a, Long b);

    List<ITestEntity> findAllByTestValueOrTestId(String a, Long b);

    @Query("select a from ITestEntity a where a.TestId = $0")
    ITestEntity querySth(Long testId);

    @Query("select a from ITestEntity a")
    List<ITestEntity> querySth();

    @Query(useValue = false)
    List<ITestEntity> querySth(String query, String match);

    Boolean softRemoveByTestIdAtTestValue(Long a, String b);

    default Boolean isB() {
        return false;
    }

}
