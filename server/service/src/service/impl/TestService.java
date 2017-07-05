package service.impl;

import data.EITest;
import orm.RepositoryManager;
import repository.ITestRepo2;
import service.ITestService;

import java.util.List;
import java.util.StringJoiner;

public class TestService implements ITestService {

//    private CustomClassLoader customClassLoader = Configs.getConfigs(Configs.CLASSLOADER, CustomClassLoader.class);
    private ITestRepo2 testRepo = RepositoryManager.getInstance().buildRepository(ITestRepo2.class);

    @Override
    public String name() {
        return "service.ITestService";
    }

    @Override
    public String test() {
        if (this.testRepo.createTableIfNotExist()) {
            List<EITest> all = testRepo.findAll();
            StringJoiner stringJoiner = new StringJoiner(", ", "{ ", " }");
            all.forEach(test -> stringJoiner.add(test.getTestString()));
            return "test x9 : " + stringJoiner.toString();

//            EITest byTestId = testRepo.findByTestId(1L);
//            return "test x3 : " + (null == byTestId ? "Null" : byTestId.toJSONString());
        }
//        Class<?> loadClass = customClassLoader.loadClass("repository.ITestRepo");
//        Class<?> aClass = null;
//        try {
//            aClass = Class.forName(loadClass.getName(), false, customClassLoader);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        return Util.util();
//        return "test x6 : " + (aClass == loadClass);
    }

    @Override
    public String toString() {
        return "TestService{}";
    }
}
