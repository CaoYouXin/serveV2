package blog.view;

import blog.data.EIComment;

import java.util.List;

public interface EICommentTree extends EICommentDetail {

    List<EICommentDetail> getLeafs();

    void setLeafs(List<EICommentDetail> leafs);

}
