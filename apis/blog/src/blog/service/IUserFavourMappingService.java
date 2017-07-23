package blog.service;

import blog.data.EIUserFavourMapping;
import rest.Service;

import java.util.List;

public interface IUserFavourMappingService extends Service {

    List<EIUserFavourMapping> list();

    EIUserFavourMapping save(EIUserFavourMapping userFavourMapping);

}
