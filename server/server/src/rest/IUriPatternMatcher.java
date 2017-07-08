package rest;

import org.apache.http.HttpRequest;

import java.util.Map;

public interface IUriPatternMatcher {

    void setController(Controller controller);

    boolean match(HttpRequest request);

    Map<String, String> getParams(HttpRequest request);

}
