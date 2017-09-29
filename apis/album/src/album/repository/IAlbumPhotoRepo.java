package album.repository;

import album.data.EIAlbumPhoto;
import album.view.EIPagedPhotos;
import orm.Query;
import orm.Repository;

import java.util.List;

public interface IAlbumPhotoRepo extends Repository<EIAlbumPhoto, Long> {

    @Query(value = "Select a From EIAlbumPhoto a Where a.UserId = $0 Order and a.AlbumPhotoDisabled = 0 By a.AlbumPhotoId Limit $1 $2")
    List<EIAlbumPhoto> queryAllByUserId(Long userId, Long offset, Integer limit);

    @Query(value = "Select count(*) as Total From EIAlbumPhoto a Where a.UserId = $0 Order and a.AlbumPhotoDisabled = 0", types = {EIAlbumPhoto.class})
    EIPagedPhotos queryAllByUserId(Long userId);

}
