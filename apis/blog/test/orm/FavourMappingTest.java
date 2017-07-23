package orm;

import beans.BeanManager;
import blog.data.EIResourceLevel;
import blog.repository.*;
import blog.view.EIResourceLevelMappingDetail;
import blog.view.EIUserFavourDetail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

class FavourMappingTest {

    @BeforeAll
    static void beforeAll() {
        DatasourceFactory.newDataSource(
                "jdbc:mysql://localhost:3306/infinitely_serve",
                "root", "root"
        );
    }

    @Test
    void test1() {
        IResourceLevelRepo resourceLevelRepo = BeanManager.getInstance().getRepository(IResourceLevelRepo.class);
        IUserFavourMappingRepo userFavourMappingRepo = BeanManager.getInstance().getRepository(IUserFavourMappingRepo.class);

        resourceLevelRepo.createTableIfNotExist();
        userFavourMappingRepo.createTableIfNotExist();

        List<EIResourceLevel> eiResourceLevels = userFavourMappingRepo.queryByThreshold(100);
        eiResourceLevels.forEach(eiResourceLevel -> {
            System.out.println(eiResourceLevel.toJSONString());
        });
    }

    @Test
    void test2() {
        IUserFavourRepo userFavourRepo = BeanManager.getInstance().getRepository(IUserFavourRepo.class);
        IResourceLevelRepo resourceLevelRepo = BeanManager.getInstance().getRepository(IResourceLevelRepo.class);
        IUserFavourMappingRepo userFavourMappingRepo = BeanManager.getInstance().getRepository(IUserFavourMappingRepo.class);

        userFavourRepo.createTableIfNotExist();
        resourceLevelRepo.createTableIfNotExist();
        userFavourMappingRepo.createTableIfNotExist();

        List<EIResourceLevel> eiResourceLevels = resourceLevelRepo.queryByUserId(1L);
        eiResourceLevels.forEach(eiResourceLevel -> {
            System.out.println(eiResourceLevel.toJSONString());
        });
    }

    @Test
    void test3() {
        IUserFavourRepo userFavourRepo = BeanManager.getInstance().getRepository(IUserFavourRepo.class);
        IUserRepo userRepo = BeanManager.getInstance().getRepository(IUserRepo.class);

        userFavourRepo.createTableIfNotExist();
        userRepo.createTableIfNotExist();

        List<EIUserFavourDetail> eiUserFavourDetails = userFavourRepo.queryAll();
        eiUserFavourDetails.forEach(eiUserFavourDetail -> {
            System.out.println(eiUserFavourDetail.toJSONString());
        });
    }

    @Test
    void test4() {
        IResourceLevelRepo resourceLevelRepo = BeanManager.getInstance().getRepository(IResourceLevelRepo.class);
        IResourceLevelMappingRepo resourceLevelMappingRepo = BeanManager.getInstance().getRepository(IResourceLevelMappingRepo.class);

        resourceLevelRepo.createTableIfNotExist();
        resourceLevelMappingRepo.createTableIfNotExist();

        List<EIResourceLevelMappingDetail> eiResourceLevelMappingDetails = resourceLevelMappingRepo.queryAll();
        eiResourceLevelMappingDetails.forEach(eiResourceLevelMappingDetail -> {
            System.out.println(eiResourceLevelMappingDetail.toJSONString());
        });
    }

}
