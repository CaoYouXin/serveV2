package rest;

public abstract class WithMatcher implements Controller {

    protected IUriPatternMatcher uriPatternMatcher;

    @Override
    public IUriPatternMatcher getUriPatternMatcher() {
        return this.uriPatternMatcher;
    }

    @Override
    public void setUriPatternMatcher(IUriPatternMatcher uriPatternMatcher) {
        this.uriPatternMatcher = uriPatternMatcher;
    }
}
