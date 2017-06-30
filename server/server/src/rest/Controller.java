package rest;

import beans.ChangeOriginI;
import org.apache.http.protocol.HttpRequestHandler;

public interface Controller extends ChangeOriginI, HttpRequestHandler {

    String name();
    String urlPattern();

    void setUriPatternMatcher(IUriPatternMatcher uriPatternMatcher);
    IUriPatternMatcher getUriPatternMatcher();

}
