package blog.service.impl;

import beans.BeanManager;
import blog.data.EIComment;
import blog.repository.ICommentRepo;
import blog.service.IBlogCommentService;
import blog.service.base.BaseService;
import blog.view.EICommentDetail;
import blog.view.EICommentTree;
import orm.Repository;

import java.util.*;
import java.util.stream.Collectors;

public class BlogCommentServiceImpl extends BaseService<EIComment, Long> implements IBlogCommentService {

    private ICommentRepo commentRepo = BeanManager.getInstance().getRepository(ICommentRepo.class);

    @Override
    public List<EICommentTree> listClientByPostId(Long postId) {
        return this.transform(this.commentRepo.queryAllEffectingByBlogPostId(postId));
    }

    @Override
    public List<EICommentTree> listAllByPostId(Long postId) {
        return this.transform(this.commentRepo.queryAllByBlogPostId(postId));
    }

    private List<EICommentTree> transform(List<EICommentDetail> allByBlogPostId) {
        Map<Long, EICommentTree> ret = new HashMap<>();

        for (EICommentDetail eiComment : allByBlogPostId) {
            if (this.hasNoParent(eiComment)) {
                EICommentTree commentTree = BeanManager.getInstance().createBean(EICommentTree.class);
                commentTree.copyFrom(eiComment);
                ret.put(eiComment.getCommentId(), commentTree);
            }
        }

        for (EICommentDetail eiComment : allByBlogPostId) {
            if (this.hasParent(eiComment)) {
                EICommentTree commentTree = ret.get(eiComment.getParentCommentId());
                if (null == commentTree) {
                    continue;
                }

                if (null == commentTree.getLeafs()) {
                    commentTree.setLeafs(new ArrayList<>());
                }

                commentTree.getLeafs().add(eiComment);
            }
        }

        return ret.values().stream().sorted((EICommentTree tree1, EICommentTree tree2) -> tree2.getCommentTime().compareTo(tree1.getCommentTime())).collect(Collectors.toList());
    }

    private boolean hasParent(EIComment eiComment) {
        return null != eiComment.getParentCommentId() && 0 != eiComment.getParentCommentId();
    }

    private boolean hasNoParent(EIComment eiComment) {
        return null == eiComment.getParentCommentId() || 0 == eiComment.getParentCommentId();
    }

    @Override
    protected Repository<EIComment, Long> getRepository() {
        return this.commentRepo;
    }

    @Override
    protected String getName() {
        return "blog comment";
    }

    @Override
    public EIComment save(EIComment data) {
        if (null == data.getCommentId()) {
            data.setCommentTime(new Date());

        } else {
            EIComment eiComment = this.commentRepo.find(data.getCommentId());
            eiComment.copyFrom(data);

            data = eiComment;
        }

        if (null == data.getCommentDisabled()) {
            data.setCommentDisabled(false);
        }

        return super.save(data);
    }
}
