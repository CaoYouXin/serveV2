package blog.service;

import blog.data.EIUserFavour;
import blog.service.exp.UserFavourException;
import blog.view.EIUserFavourDetail;
import rest.Service;

import java.util.List;

public interface IUserFavourService extends Service {

    List<EIUserFavourDetail> list();

    EIUserFavour save(EIUserFavour userFavour) throws UserFavourException;

    Boolean increaseFavour(Long userId, int inc);

}
