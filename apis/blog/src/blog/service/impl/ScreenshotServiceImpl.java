package blog.service.impl;

import beans.BeanManager;
import blog.data.EIScreenshot;
import blog.repository.IScreenshotRepo;
import blog.service.IScreenshotService;
import blog.service.base.BaseService;
import orm.Repository;

import java.util.List;

public class ScreenshotServiceImpl extends BaseService<EIScreenshot, Long> implements IScreenshotService {

    private IScreenshotRepo screenshotRepo = BeanManager.getInstance().getRepository(IScreenshotRepo.class);

    @Override
    protected Repository<EIScreenshot, Long> getRepository() {
        return this.screenshotRepo;
    }

    @Override
    protected String getName() {
        return "blog screenshot";
    }

    @Override
    public List<EIScreenshot> listByPostId(Long postId) {
        return this.screenshotRepo.findAllByBlogPostIdAndScreenshotDisabled(postId, false);
    }
}
