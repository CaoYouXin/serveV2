package orm;

import java.util.List;

@RepositoryName(value = "ITestRepo")
public interface ITestRepo extends Repository<ITestEntity, Long> {

    ITestEntity findByTestValueOrTestId(String a, Long b);

    List<ITestEntity> findAllByTestValueOrTestId(String a, Long b);

    @Query(value = "select")
    ITestEntity querySth();

    Boolean softRemoveByTestIdAtTestValue(Long a, String b);

}
