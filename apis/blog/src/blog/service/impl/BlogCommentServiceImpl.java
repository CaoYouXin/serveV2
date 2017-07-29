package blog.service.impl;

import beans.BeanManager;
import blog.data.EIComment;
import blog.repository.ICommentRepo;
import blog.service.IBlogCommentService;
import blog.service.base.BaseService;
import blog.view.EICommentTree;
import orm.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlogCommentServiceImpl extends BaseService<EIComment, Long> implements IBlogCommentService {

    private ICommentRepo commentRepo = BeanManager.getInstance().getRepository(ICommentRepo.class);

    @Override
    public List<EICommentTree> listByPostId(Long postId) {

        Map<Long, EICommentTree> ret = new HashMap<>();

        List<EIComment> allByBlogPostId = this.commentRepo.queryAllByBlogPostId(postId);
        for (EIComment eiComment : allByBlogPostId) {
            if (null == eiComment.getParentCommentId()) {
                EICommentTree commentTree = BeanManager.getInstance().createBean(EICommentTree.class);
                commentTree.copyFrom(eiComment);
                ret.put(eiComment.getCommentId(), commentTree);
            }
        }

        for (EIComment eiComment : allByBlogPostId) {
            if (null != eiComment.getParentCommentId()) {
                EICommentTree commentTree = ret.get(eiComment.getParentCommentId());
                if (null == commentTree.getLeafs()) {
                    commentTree.setLeafs(new ArrayList<>());
                }

                commentTree.getLeafs().add(eiComment);
            }
        }

        return new ArrayList<>(ret.values());
    }

    @Override
    protected Repository<EIComment, Long> getRepository() {
        return this.commentRepo;
    }

    @Override
    protected String getName() {
        return "blog comment";
    }
}
