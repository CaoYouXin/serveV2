package orm;

@RepositoryName(value = "ITestRepo")
public interface ITestRepo extends Repository<ITestEntity, Long> {

    ITestEntity findByTestValueOrTestId(String a, Long b);

    void updateOther(String a);

}
