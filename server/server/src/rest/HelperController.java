package rest;

public abstract class HelperController implements Controller {

    protected UriPatternMatcher uriPatternMatcher;

    @Override
    public void setUriPatternMatcher(UriPatternMatcher uriPatternMatcher) {
        this.uriPatternMatcher = uriPatternMatcher;
    }

    @Override
    public UriPatternMatcher getUriPatternMatcher() {
        return this.uriPatternMatcher;
    }
}
