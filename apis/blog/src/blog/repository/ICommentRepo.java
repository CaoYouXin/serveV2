package blog.repository;

import blog.data.EIComment;
import blog.data.EIUser;
import blog.view.EICommentDetail;
import orm.Query;
import orm.Repository;

import java.util.List;

public interface ICommentRepo extends Repository<EIComment, Long> {

    @Query(value = "select a, b.UserName WriterName, c.UserName CommenteeName from EIComment a left join EIUser b on a.WriterUserId = b.UserId left join EIUser c on a.CommenteeUserId = c.UserId where a.BlogPostId = $0 and a.CommentDisabled = 0 order by a.CommentTime desc", types = {EIComment.class, EIUser.class})
    List<EICommentDetail> queryAllEffectingByBlogPostId(Long postId);

    @Query(value = "select a, b.UserName WriterName, c.UserName CommenteeName from EIComment a left join EIUser b on a.WriterUserId = b.UserId left join EIUser c on a.CommenteeUserId = c.UserId where a.BlogPostId = $0 order by a.CommentTime desc", types = {EIComment.class, EIUser.class})
    List<EICommentDetail> queryAllByBlogPostId(Long postId);

}
