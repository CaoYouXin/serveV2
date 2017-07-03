package orm;

import beans.BeanManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

class BuilderTest {

    @BeforeAll
    static void beforeAll() {
        DatasourceFactory.newDataSource(
                "jdbc:mysql://localhost:3306/infinitely_serve",
                "root", "root"
        );
    }

    @Test
    void test1() {
        ITestRepo iTestRepo = RepositoryManager.getInstance().buildRepository(ITestRepo.class);
        ITestEntity iTestEntity = iTestRepo.find(1L);
        System.out.println(null == iTestEntity ? "NULL" : iTestEntity.toJSONString());
        iTestEntity = iTestRepo.findByTestValueOrTestId("asdf", 3L);
        System.out.println(null == iTestEntity ? "NULL" : iTestEntity.toJSONString());
        iTestRepo.updateOther("CCC");
    }

    @Test
    void test2() {
        ITestRepo iTestRepo = RepositoryManager.getInstance().buildRepository(ITestRepo.class);
        if (iTestRepo.createTableIfNotExist()) {
            ITestEntity iTestEntity = iTestRepo.find(2L);
            System.out.println(null == iTestEntity ? "NULL" : iTestEntity.toJSONString());
        }
    }

    @Test
    void test3() {
        ITestRepo iTestRepo = RepositoryManager.getInstance().buildRepository(ITestRepo.class);
        List<ITestEntity> all = iTestRepo.findAll();
        all.forEach(iTestEntity ->
                System.out.println(null == iTestEntity ? "NULL" : iTestEntity.toJSONString()));
    }

    @Test
    void test4() {
        ITestRepo iTestRepo = RepositoryManager.getInstance().buildRepository(ITestRepo.class);
        ITestEntity testEntity = BeanManager.getInstance().createBean(ITestEntity.class);
        testEntity.setTestValue("ABCD");
        System.out.println(testEntity.toJSONString());
        iTestRepo.save(testEntity);
        System.out.println(testEntity.toJSONString());
    }

    @Test
    void test5() {
        ITestRepo iTestRepo = RepositoryManager.getInstance().buildRepository(ITestRepo.class);
        ITestEntity testEntity = BeanManager.getInstance().createBean(ITestEntity.class);
        testEntity.setTestValue("AAAA");
        testEntity.setTestId(5L);
        iTestRepo.save(testEntity);
        System.out.println(testEntity.toJSONString());
    }

}
