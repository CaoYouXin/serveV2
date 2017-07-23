package blog.service;

import blog.data.EIResourceLevel;
import rest.Service;

import java.util.List;

public interface IResourceLevelService extends Service {

    List<EIResourceLevel> list();

    EIResourceLevel save(EIResourceLevel resourceLevel);

}
