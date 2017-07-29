package blog.view;

import blog.data.EIComment;

import java.util.List;

public interface EICommentTree extends EIComment {

    List<EIComment> getLeafs();

    void setLeafs(List<EIComment> leafs);

}
