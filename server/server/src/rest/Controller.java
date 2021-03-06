package rest;

import beans.ChangeOriginI;
import org.apache.http.protocol.HttpRequestHandler;

public interface Controller extends ChangeOriginI, HttpRequestHandler {

    default int auth() {
        return 0;
    }

    String name();

    String urlPattern();

    IUriPatternMatcher getUriPatternMatcher();

    void setUriPatternMatcher(IUriPatternMatcher uriPatternMatcher);

}
