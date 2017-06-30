package rest;

public abstract class HelperController implements Controller {

    protected IUriPatternMatcher uriPatternMatcher;

    @Override
    public void setUriPatternMatcher(IUriPatternMatcher uriPatternMatcher) {
        this.uriPatternMatcher = uriPatternMatcher;
    }

    @Override
    public IUriPatternMatcher getUriPatternMatcher() {
        return this.uriPatternMatcher;
    }
}
