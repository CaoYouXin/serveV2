package blog.repository;

import blog.data.EIComment;
import orm.Query;
import orm.Repository;

import java.util.List;

public interface ICommentRepo extends Repository<EIComment, Long> {

    @Query("select a from EIComment a where a.BlogPostId = $0 and a.CommentDisabled = 0 order by a.CommentTime desc")
    List<EIComment> queryAllEffectingByBlogPostId(Long postId);

    @Query("select a from EIComment a where a.BlogPostId = $0 order by a.CommentTime desc")
    List<EIComment> queryAllByBlogPostId(Long postId);

}
