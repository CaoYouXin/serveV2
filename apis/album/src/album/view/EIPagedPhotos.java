package album.view;

import album.data.EIAlbumPhoto;
import beans.EntityBeanI;

import javax.persistence.Column;
import java.util.List;

public interface EIPagedPhotos extends EntityBeanI {

    List<EIAlbumPhoto> getPhotos();

    void setPhotos(List<EIAlbumPhoto> photos);

    @Column
    Integer getTotal();

    void setTotal(Integer total);

}
