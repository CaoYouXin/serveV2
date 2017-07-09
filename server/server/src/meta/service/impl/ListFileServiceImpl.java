package meta.service.impl;

import beans.BeanManager;
import meta.service.IListFileService;
import meta.view.EIFileInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListFileServiceImpl implements IListFileService {
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
}
