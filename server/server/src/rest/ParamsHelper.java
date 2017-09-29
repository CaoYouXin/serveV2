package rest;

import java.util.Map;

public class ParamsHelper {

    private final Map<String, String> data;

    ParamsHelper(Map<String, String> data) {
        this.data = data;
    }

    public Long getLong(String key) {
        return Long.parseLong(this.data.get(key));
    }

    public Integer getInteger(String key) {
        return Integer.parseInt(this.data.get(key));
    }

}
