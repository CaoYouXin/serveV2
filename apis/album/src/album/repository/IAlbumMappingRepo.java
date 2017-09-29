package album.repository;

import album.data.EIAlbumMapping;
import orm.Repository;

public interface IAlbumMappingRepo extends Repository<EIAlbumMapping, Long> {

    EIAlbumMapping findByAlbumIdAndAlbumPhotoId(Long albumId, Long photoId);

}
