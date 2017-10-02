package blog.repository;

import blog.data.EILikeRec;
import blog.view.EICount;
import orm.Query;
import orm.Repository;

import java.util.List;

public interface ILikeRecRepo extends Repository<EILikeRec, Long> {

    @Query(value = "select a.BlogPostId from EILikeRec a group by a.BlogPostId order by count(a.LikeId) desc limit 5")
    List<EILikeRec> queryTop5();

    EILikeRec findByUserIdAndBlogPostId(Long userId, Long postId);

    @Query(value = "select count(a.LikeId) Count from EILikeRec a where a.BlogPostId = :0", types = {EILikeRec.class})
    EICount queryCount(Long postId);

}
