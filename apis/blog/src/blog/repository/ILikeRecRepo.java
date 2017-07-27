package blog.repository;

import blog.data.EILikeRec;
import orm.Query;
import orm.Repository;

import java.util.List;

public interface ILikeRecRepo extends Repository<EILikeRec, Long> {

    @Query(value = "select a.BlogPostId from EILikeRec a group by a.BlogPostId order by count(a.LikeId) desc limit 5")
    List<EILikeRec> queryTop5();

}
