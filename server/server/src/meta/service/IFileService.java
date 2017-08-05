package meta.service;

import meta.view.EIFileInfo;
import rest.Service;

import java.io.File;
import java.util.List;

public interface IFileService extends Service {

    List<EIFileInfo> getChildren(File file);

    Boolean delete(File file);

    Boolean createDir(File file);

    Boolean copy(List<String> src, String dst);
}
