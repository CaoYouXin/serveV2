package rest;

import com.sun.jndi.toolkit.url.Uri;
import org.apache.http.HttpRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.StringUtil;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UriPatternMatcher implements IUriPatternMatcher {

    private static final Logger logger = LogManager.getLogger(UriPatternMatcher.class);

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
            stringBuilder.append(urlPattern.substring(startSub, start).replaceAll("\\?", "\\\\?"));

            end = StringUtil.indexOf(urlPattern, start, '/', '&', '?');

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
            stringBuilder.append(urlPattern.substring(end).replaceAll("\\?", "\\?"));
        }

        String regex = this.prefix + stringBuilder.toString();

        logger.info("reg : " + regex);

        this.pattern = Pattern.compile(regex);
    }

    @Override
    public boolean match(HttpRequest request, Map<String, String> paramsNotCache) {
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
            if (null != paramsNotCache) {
                paramsNotCache.putAll(params);
            }
        }

        return matches;
    }

    @Override
    public Map<String, String> getParams(HttpRequest request) {
        Map<String, String> ret = this.paramsCache.get(request.getRequestLine().getUri());
        if (null == ret) {
            ret = new HashMap<>();
            if (this.match(request, ret)) {
                return ret;
            }
        }

        return ret;
    }
}
