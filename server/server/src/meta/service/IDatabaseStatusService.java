package meta.service;

import java.io.File;
import java.util.List;

public interface IDatabaseStatusService {

    File[] listCfgFiles();
    Boolean cpCfg2Active(File cfg);
    Boolean cpCfg2Active(String cfg);
    File activeCfgFile();
    Boolean testCfgConn(File cfg);

}
