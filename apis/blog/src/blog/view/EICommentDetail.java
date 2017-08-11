package blog.view;

import blog.data.EIComment;

import javax.persistence.Column;

public interface EICommentDetail extends EIComment {

    @Column
    String getWriterName();

    void setWriterName(String writerName);

    @Column
    String getCommenteeName();

    void setCommenteeName(String commenteeName);

}
