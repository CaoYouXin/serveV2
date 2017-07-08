package meta.service;

import rest.Service;

import java.io.File;

public interface IDatabaseStatusService extends Service {

    File[] listCfgFiles();

    Boolean cpCfg2Active(File cfg);

    Boolean cpCfg2Active(String cfg, String backup);

    File activeCfgFile();

    Boolean testCfgConn(File cfg);

}
