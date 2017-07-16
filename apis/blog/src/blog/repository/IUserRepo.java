package blog.repository;

import blog.data.EIUser;
import orm.Repository;

public interface IUserRepo extends Repository<EIUser, Long> {

    EIUser findByUserName(String userName);

}
