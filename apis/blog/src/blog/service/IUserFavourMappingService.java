package blog.service;

import blog.data.EIUserFavourMapping;
import blog.view.EIUserFavourMappingDetail;
import rest.Service;

import java.util.List;

public interface IUserFavourMappingService extends Service {

    List<EIUserFavourMappingDetail> list();

    EIUserFavourMapping save(EIUserFavourMapping userFavourMapping);

}
