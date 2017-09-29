package album.repository;

import album.data.EIAlbumAlbum;
import orm.Repository;

import java.util.List;

public interface IAlbumAlbumRepo extends Repository<EIAlbumAlbum, Long> {

    List<EIAlbumAlbum> findAllByUserIdAndAlbumDisabled(Long userId, boolean disabled);

}
