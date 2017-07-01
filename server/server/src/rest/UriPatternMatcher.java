package rest;

import org.apache.http.HttpRequest;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UriPatternMatcher implements IUriPatternMatcher {

    private String prefix;
    private Pattern pattern;
    private List<String> paramNames;
    private Map<String, Map<String, String>> paramsCache;
    private Queue<String> paramsCacheSize;

    public UriPatternMatcher(String prefix) {
        this.prefix = prefix;
        this.paramNames = new ArrayList<>();
        this.paramsCache = new HashMap<>();
        this.paramsCacheSize = new PriorityQueue<>();
    }

    @Override
    public void setController(Controller controller) {
        StringBuilder stringBuilder = new StringBuilder();

        String urlPattern = controller.urlPattern();
        int start = urlPattern.indexOf(':'), end = 0, startSub = 0;
        while (-1 != start) {
            stringBuilder.append(urlPattern.substring(startSub, start));

            end = urlPattern.indexOf('/', start);
            if (-1 == end) {
                end = urlPattern.indexOf('&', start);
            }

            String paramName;
            if (-1 == end) {
                paramName = urlPattern.substring(start + 1);
                end = urlPattern.length();
            } else {
                paramName = urlPattern.substring(start + 1, end);
            }
            this.paramNames.add(paramName);

            stringBuilder.append("(?<")
                    .append(paramName)
                    .append(">.*?)");

            start = urlPattern.indexOf(':', end);
            startSub = end;
        }
        if (end != urlPattern.length()) {
            stringBuilder.append(urlPattern.substring(end));
        }

        String regex = this.prefix + stringBuilder.toString();
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public boolean match(HttpRequest request) {
        String uri = request.getRequestLine().getUri();
        String decodedUrl = RestHelper.getDecodedUrl(request);
        Matcher matcher = this.pattern.matcher(decodedUrl);
        boolean matches = matcher.matches();

        if (matches) {
            Map<String, String> params = new HashMap<>();
            for (String paramName : this.paramNames) {
                params.put(paramName, matcher.group(paramName));
            }
            if (!this.paramsCache.keySet().contains(uri)) {
                this.paramsCacheSize.add(uri);
                if (this.paramsCacheSize.size() > 100) {
                    String remove = this.paramsCacheSize.remove();
                    this.paramsCache.remove(remove);
                }
            }
            this.paramsCache.put(uri, params);
        }

        return matches;
    }

    @Override
    public Map<String, String> getParams(HttpRequest request) {
        return this.paramsCache.get(request.getRequestLine().getUri());
    }
}
