package repository;

import data.EITest;
import orm.Repository;

public interface ITestRepo extends Repository<EITest, Long> {

    EITest findByTestId(Long a);

    EITest findByTestString(String a);

}
