package blog.service.impl;

import beans.BeanManager;
import blog.data.EIScreenshot;
import blog.repository.IScreenshotRepo;
import blog.service.IScreenshotService;
import blog.service.base.BaseService;
import orm.Repository;

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
}
