package album.repository;

import album.data.EIAlbumMapping;
import album.data.EIAlbumPhoto;
import album.view.EIPagedPhotos;
import orm.Query;
import orm.Repository;

import java.util.List;

public interface IAlbumPhotoRepo extends Repository<EIAlbumPhoto, Long> {

    @Query(value = "Select a From EIAlbumPhoto a Where a.UserId = :0 and a.AlbumPhotoDisabled = 0 Order By a.AlbumPhotoId Limit :1, :2")
    List<EIAlbumPhoto> queryAllByUserId(Long userId, Long offset, Integer limit);

    @Query(value = "Select count(*) Total From EIAlbumPhoto a Where a.UserId = :0 and a.AlbumPhotoDisabled = 0", types = {EIAlbumPhoto.class})
    EIPagedPhotos queryAllByUserId(Long userId);

    @Query(value = "Select a From EIAlbumPhoto a, (Select c From EIAlbumMapping c Where c.AlbumId = :0 and c.AlbumMappingDisabled = 0) b Where a.AlbumPhotoId = b.AlbumPhotoId Order By a.AlbumPhotoId Limit :1, :2", types = {EIAlbumMapping.class})
    List<EIAlbumPhoto> queryAllByAlbumId(Long albumId, Long offset, Integer limit);

}
