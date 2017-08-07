package blog.service;

import blog.data.EIComment;
import blog.view.EICommentTree;
import rest.Service;

import java.util.List;

public interface IBlogCommentService extends Service {

    EIComment save(EIComment comment);

    List<EICommentTree> listClientByPostId(Long postId);

    List<EICommentTree> listAllByPostId(Long postId);

}
