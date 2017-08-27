package blog.service;

import blog.service.exp.BlogLikeException;
import rest.Service;

public interface IBlogLikeService extends Service {

    Boolean like(Long postId, Long userId) throws BlogLikeException;

    Integer likeCount(Long postId);

    Boolean liked(Long userId, Long postId);

}
