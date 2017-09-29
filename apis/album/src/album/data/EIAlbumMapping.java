package album.data;

import beans.EntityBeanI;

import javax.persistence.*;

@Entity(name = "album_mapping")
public interface EIAlbumMapping extends EntityBeanI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getAlbumMappingId();

    void setAlbumMappingId(Long albumMappingId);

    @Column(name = "album_id", nullable = false)
    Long getAlbumId();

    void setAlbumId(Long albumId);

    @Column(name = "album_photo_id", nullable = false)
    Long getAlbumPhotoId();

    void setAlbumPhotoId(Long albumPhotoId);

    @Column(name = "disabled", nullable = false)
    Boolean isAlbumMappingDisabled();

    void setAlbumMappingDisabled(Boolean albumMappingDisabled);

}
