package rest;

import org.apache.http.HttpRequest;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UriPatternMatcher implements IUriPatternMatcher {

    private String prefix;
    private Pattern pattern;

    public UriPatternMatcher(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void setController(Controller controller) {
        this.pattern = Pattern.compile(this.prefix + controller.urlPattern());
    }

    @Override
    public boolean match(HttpRequest request) {
        Matcher matcher = this.pattern.matcher(request.getRequestLine().getUri());
        return matcher.matches();
    }

    @Override
    public Map<String, String> getParams(HttpRequest request) {
        return null;
    }
}
