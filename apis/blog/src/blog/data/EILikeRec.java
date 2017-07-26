package blog.data;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "blog_like_record")
public interface EILikeRec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getLikeId();

    void setLikeId(Long likeId);

    @Column(name = "user_id", nullable = false)
    Long getUserId();

    void setUserId(Long userId);

    @Column(name = "post_id", nullable = false)
    Long getBlogPostId();

    void setBlogPostId(Long blogPostId);

    @Column(name = "create", nullable = false)
    Date getLikeTime();

    void setLikeTime(Date likeTime);

}
