package config;

public class InitConfig {

    public static final String CONFIG_KEY = "init-configs";

    private int port;
    private String classpath;
    private String configRoot;
    private String deployRoot;
    private String uploadRoot;
    private String resourceRoot;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getClasspath() {
        return classpath;
    }

    public void setClasspath(String classpath) {
        this.classpath = classpath;
    }

    public String getConfigRoot() {
        return configRoot;
    }

    public void setConfigRoot(String configRoot) {
        this.configRoot = configRoot;
    }

    public String getDeployRoot() {
        return deployRoot;
    }

    public void setDeployRoot(String deployRoot) {
        this.deployRoot = deployRoot;
    }

    public String getUploadRoot() {
        return uploadRoot;
    }

    public void setUploadRoot(String uploadRoot) {
        this.uploadRoot = uploadRoot;
    }

    public String getResourceRoot() {
        return resourceRoot;
    }

    public void setResourceRoot(String resourceRoot) {
        this.resourceRoot = resourceRoot;
    }

    @Override
    public String toString() {
        return "InitConfig{" +
                "port=" + port +
                ", classpath='" + classpath + '\'' +
                ", configRoot='" + configRoot + '\'' +
                ", deployRoot='" + deployRoot + '\'' +
                ", uploadRoot='" + uploadRoot + '\'' +
                ", resourceRoot='" + resourceRoot + '\'' +
                '}';
    }

}
