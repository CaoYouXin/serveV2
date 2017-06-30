package rest;

import org.apache.http.HttpRequest;

import java.util.Map;

public interface UriPatternMatcher {

    void setUrlPattern(String urlPattern);
    boolean match(HttpRequest request);
    Map<String, String> getParams(HttpRequest request);

}
