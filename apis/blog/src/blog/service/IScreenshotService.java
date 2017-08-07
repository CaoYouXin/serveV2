package blog.service;

import blog.data.EIScreenshot;
import rest.Service;

import java.util.List;

public interface IScreenshotService extends Service {

    List<EIScreenshot> list();

    EIScreenshot save(EIScreenshot screenshot);

    List<EIScreenshot> listByPostId(Long postId);

}
