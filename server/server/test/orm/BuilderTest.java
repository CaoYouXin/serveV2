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
        List<ITestEntity> all = iTestRepo.findAllByTestValueOrTestId("asdf", 3L);
        all.forEach(testEntity ->
                System.out.println(null == testEntity ? "NULL" : testEntity.toJSONString()));
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
        if (iTestRepo.save(testEntity)) {
            System.out.println(testEntity.toJSONString());
        }
    }

    @Test
    void test5() {
        ITestRepo iTestRepo = RepositoryManager.getInstance().buildRepository(ITestRepo.class);
        ITestEntity testEntity = BeanManager.getInstance().createBean(ITestEntity.class);
        testEntity.setTestValue("AAAA");
        testEntity.setTestId(5L);
        if (iTestRepo.save(testEntity)) {
            System.out.println(testEntity.toJSONString());
        }
    }

    @Test
    void test6() {
        ITestRepo iTestRepo = RepositoryManager.getInstance().buildRepository(ITestRepo.class);
        ITestEntity iTestEntity = iTestRepo.querySth(5L);
        System.out.println(null == iTestEntity ? "NULL" : iTestEntity.toJSONString());
    }

    @Test
    void test7() {
        ITestRepo iTestRepo = RepositoryManager.getInstance().buildRepository(ITestRepo.class);
        System.out.println(iTestRepo.remove(4L));
    }

    @Test
    void test8() {
        ITestRepo iTestRepo = RepositoryManager.getInstance().buildRepository(ITestRepo.class);
        System.out.println(iTestRepo.softRemoveByTestIdAtTestValue(5L, "DELETED"));
    }

    @Test
    void test9() {
        ITestRepo iTestRepo = RepositoryManager.getInstance().buildRepository(ITestRepo.class);
        System.out.println(iTestRepo.isB());
    }

    @Test
    void test10() {
        ITestRepo iTestRepo = RepositoryManager.getInstance().buildRepository(ITestRepo.class);
        List<ITestEntity> all = iTestRepo.querySth();
        all.forEach(iTestEntity -> System.out.println(null == iTestEntity ? "NULL" : iTestEntity.toJSONString()));
    }

    @Test
    void test11() {
        ITestRepo iTestRepo = RepositoryManager.getInstance().buildRepository(ITestRepo.class);
        List<ITestEntity> all = iTestRepo.querySth("select a from ITestEntity a where a.TestValue != $0", "DELETED");
        all.forEach(iTestEntity -> System.out.println(null == iTestEntity ? "NULL" : iTestEntity.toJSONString()));
    }

}
