package meta.service.impl;

import config.Configs;
import config.InitConfig;
import meta.service.IResourceService;
import meta.service.exp.ResourceTransformException;

import java.util.HashMap;
import java.util.Map;

public class ResourceServiceImpl implements IResourceService {

    private static final Map<String, String> CONFIGS = new HashMap<>();

    static {
        InitConfig initConfig = Configs.getConfigs(InitConfig.CONFIG_KEY, InitConfig.class);
        CONFIGS.put("classpath", initConfig.getClasspath());
        CONFIGS.put("config", initConfig.getConfigRoot());
        CONFIGS.put("serve", initConfig.getResourceRoot());
        CONFIGS.put("upload", initConfig.getUploadRoot());
    }

    @Override
    public String transform(String resourceName) {
        return CONFIGS.get(resourceName);
    }

    @Override
    public String transformFromPath(String path) throws ResourceTransformException {
        int beginIndex = path.indexOf('/');
        String head = path.substring(0, beginIndex);

        String transformed = this.transform(head);
        if (null == transformed) {
            throw new ResourceTransformException(head + " is not a resource name.");
        }

        return transformed + path.substring(beginIndex + 1);
    }
}
