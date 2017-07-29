package blog.service.impl;

import beans.BeanManager;
import blog.data.EILikeRec;
import blog.repository.ILikeRecRepo;
import blog.service.IBlogLikeService;
import blog.service.exp.BlogLikeException;
import blog.service.exp.TableNotCreateException;
import blog.view.EICount;
import orm.DatasourceFactory;

import java.sql.Connection;
import java.util.Date;

public class BlogLikeServiceImpl implements IBlogLikeService {

    private ILikeRecRepo likeRecRepo = BeanManager.getInstance().getRepository(ILikeRecRepo.class);

    @Override
    public void before() {
        if (!this.likeRecRepo.createTableIfNotExist()) {
            throw new TableNotCreateException("like record");
        }
    }

    @Override
    public Boolean like(Long postId, Long userId) throws BlogLikeException {
        DatasourceFactory.begin(Connection.TRANSACTION_SERIALIZABLE);

        EILikeRec byUserIdAndBlogPostId = likeRecRepo.findByUserIdAndBlogPostId(userId, postId);
        if (null != byUserIdAndBlogPostId) {
            DatasourceFactory.rollback();
            throw new BlogLikeException("无需重复赞同一篇文章.");
        }

        byUserIdAndBlogPostId = BeanManager.getInstance().createBean(EILikeRec.class);
        byUserIdAndBlogPostId.setBlogPostId(postId);
        byUserIdAndBlogPostId.setUserId(userId);
        byUserIdAndBlogPostId.setLikeTime(new Date());
        Boolean save = this.likeRecRepo.save(byUserIdAndBlogPostId);
        DatasourceFactory.commit();

        return save;
    }

    @Override
    public Integer likeCount(Long postId) {
        return this.likeRecRepo.queryCount(postId).getCount();
    }
}
