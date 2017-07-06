package meta.service.impl;

import beans.BeanManager;
import meta.data.EIConfig;
import meta.repository.IConfigRepo;
import meta.service.IAdminService;
import meta.service.exp.AdminSettingException;
import orm.RepositoryManager;

public class AdminServiceImpl implements IAdminService {

    private IConfigRepo configRepo = BeanManager.getInstance().getRepository(IConfigRepo.class);

    @Override
    public Boolean check() {
        if (configRepo.createTableIfNotExist()) {
            EIConfig adminUserName = configRepo.findByConfigKey("admin.username");
            if (null == adminUserName) {
                return false;
            }

            EIConfig adminPassword = configRepo.findByConfigKey("admin.password");
            if (null == adminPassword) {
                return false;
            }

            return true;
        }

        return null;
    }

    @Override
    public Boolean setting(String oldUserName, String username, String oldPassword, String password) throws AdminSettingException {
        if (configRepo.createTableIfNotExist()) {
            EIConfig adminUserName = configRepo.findByConfigKey("admin.username");
            if (null == adminUserName) {
                adminUserName = BeanManager.getInstance().createBean(EIConfig.class);
                adminUserName.setConfigKey("admin.username");
            } else {
                if (!adminUserName.getConfigValue().equals(oldUserName)) {
                    throw new AdminSettingException("admin username not matched.");
                }
            }
            adminUserName.setConfigValue(username);

            EIConfig adminPassword = configRepo.findByConfigKey("admin.password");
            if (null == adminPassword) {
                adminPassword = BeanManager.getInstance().createBean(EIConfig.class);
                adminPassword.setConfigKey("admin.password");
            } else {
                if (!adminPassword.getConfigValue().equals(oldPassword)) {
                    throw new AdminSettingException("admin password not matched.");
                }
            }
            adminPassword.setConfigValue(password);

            return configRepo.save(adminUserName) && configRepo.save(adminPassword);
        }

        return null;
    }
}
