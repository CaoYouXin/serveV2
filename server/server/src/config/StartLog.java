package config;

public class StartLog {

    public static final String CONFIG_KEY = "start-log-token";

    private String host;
    private int port;
    private String token;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "StartLog{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", token='" + token + '\'' +
                '}';
    }

}
