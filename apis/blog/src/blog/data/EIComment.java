package blog.data;

import beans.EntityBeanI;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "blog_comment")
public interface EIComment extends EntityBeanI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getCommentId();

    void setCommentId(Long commentId);

    @Column(name = "post_id")
    Long getBlogPostId();

    void setBlogPostId(Long blogPostId);

    @Column(name = "pid")
    Long getParentCommentId();

    void setParentCommentId(Long parentCommentId);

    @Column(name = "writer", nullable = false)
    Long getWriterUserId();

    void setWriterUserId(Long writerUserId);

    @Column(name = "commented")
    Long getCommentedUserId();

    void setCommentedUserId(Long commentedUserId);

    @Column(name = "create", nullable = false)
    Date getCommentTime();

    void setCommentTime(Date commentTime);

    @Column(name = "content", length = 10240, nullable = false)
    String getCommentContent();

    void setCommentContent(String commentContent);

    @Column(name = "disabled", nullable = false)
    Boolean getCommentDisabled();

    void setCommentDisabled(Boolean commentDisabled);

}
