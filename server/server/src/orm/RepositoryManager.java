package orm;

public class RepositoryManager {

    private static final RepositoryManager INSTANCE = new RepositoryManager();

    public static RepositoryManager getInstance() {
        return INSTANCE;
    }

    private RepositoryManager() {}

    public <T extends Repository> T buildRepository(Class<? extends T> proto) {
        return null;
    }

}
