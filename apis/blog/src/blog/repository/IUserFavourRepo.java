package blog.repository;

import blog.data.EIUser;
import blog.data.EIUserFavour;
import blog.view.EIUserFavourDetail;
import orm.Query;
import orm.Repository;

import java.util.List;

public interface IUserFavourRepo extends Repository<EIUserFavour, Long> {

    @Query(value = "select a.UserId, a.UserName, a.UserNickName, a.UserPhone, a.UserSex, a.UserAge, a.UserProfession, a.UserAvatar, b.UserFavourValue from EIUserFavour b, EIUser a where a.UserId = b.UserId and a.UserDisabled = 0", types = {EIUserFavour.class, EIUser.class})
    List<EIUserFavourDetail> queryAll();

    EIUserFavour findByUserId(Long userId);

}
