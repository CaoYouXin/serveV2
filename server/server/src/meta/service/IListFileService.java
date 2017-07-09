package meta.service;

import meta.view.EIFileInfo;
import rest.Service;

import java.io.File;
import java.util.List;

public interface IListFileService extends Service {

    List<EIFileInfo> getChildren(File file);

}
