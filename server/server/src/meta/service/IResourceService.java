package meta.service;

import meta.service.exp.ResourceTransformException;
import rest.Service;

public interface IResourceService extends Service {

    String transform(String resourceName);

    String transformFromPath(String path) throws ResourceTransformException;

}
