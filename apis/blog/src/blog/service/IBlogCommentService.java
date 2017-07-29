package blog.service;

import blog.data.EIComment;
import blog.view.EICommentTree;
import rest.Service;

import java.util.List;

public interface IBlogCommentService extends Service {

    List<EIComment> list();

    EIComment save(EIComment comment);

    List<EICommentTree> listByPostId(Long postId);

}
