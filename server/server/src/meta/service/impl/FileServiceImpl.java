package meta.service.impl;

import beans.BeanManager;
import meta.service.IFileService;
import meta.view.EIFileInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import task.FileCopy;
import task.FileDel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileServiceImpl implements IFileService {

    private static final Logger logger = LogManager.getLogger(FileServiceImpl.class);

    @Override
    public List<EIFileInfo> getChildren(File file) {
        List<EIFileInfo> ret = new ArrayList<>();
        for (File child : file.listFiles()) {
            EIFileInfo bean = BeanManager.getInstance().createBean(EIFileInfo.class);
            ret.add(bean);

            bean.setDir(child.isDirectory());
            bean.setName(child.getName());
        }
        return ret;
    }

    @Override
    public Boolean delete(File file) {
        try {
            return new FileDel(file).call();
        } catch (Exception e) {
            logger.catching(e);
            return false;
        }
    }

    @Override
    public Boolean createDir(File file) {
        if (!file.mkdirs()) {
            return false;
        }

        return true;
    }

    @Override
    public Boolean copy(List<String> src, String dst) {
        boolean ret = true;
        try {
            for (String s : src) {
                if (ret) {
                    ret = new FileCopy(s, dst).call();
                }
            }
        } catch (Exception e) {
            return false;
        }
        return ret;
    }
}
