package meta.service;

import java.io.File;

public interface IDatabaseStatusService {

    File[] listCfgFiles();
    Boolean cpCfg2Active(File cfg);
    Boolean cpCfg2Active(String cfg, File backup);
    File activeCfgFile();
    Boolean testCfgConn(File cfg);

}
