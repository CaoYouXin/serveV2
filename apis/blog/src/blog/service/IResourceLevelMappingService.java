package blog.service;

import blog.data.EIResourceLevelMapping;
import rest.Service;

import java.util.List;

public interface IResourceLevelMappingService extends Service {

    List<EIResourceLevelMapping> list();

    EIResourceLevelMapping save(EIResourceLevelMapping resourceLevelMapping);

}
