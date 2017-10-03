package album.repository;

import album.data.EIAlbumMapping;
import album.view.EIPagedPhotos;
import orm.Query;
import orm.Repository;

import java.util.List;

public interface IAlbumMappingRepo extends Repository<EIAlbumMapping, Long> {

    EIAlbumMapping findByAlbumIdAndAlbumPhotoId(Long albumId, Long photoId);

    List<EIAlbumMapping> findAllByAlbumIdAndAlbumMappingDisabled(Long albumId, Boolean disabled);

    @Query(value = "Select count(*) Total From EIAlbumMapping a Where a.AlbumId = :0 and a.AlbumMappingDisabled = 0", types = {EIAlbumMapping.class})
    EIPagedPhotos queryAllByAlbumId(Long albumId);

}
