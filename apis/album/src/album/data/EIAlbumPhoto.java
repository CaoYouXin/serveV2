package album.data;

import beans.EntityBeanI;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "album_photo")
public interface EIAlbumPhoto extends EntityBeanI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getAlbumPhotoId();

    void setAlbumPhotoId(Long albumPhotoId);

    @Column(name = "user_id", nullable = false)
    Long getUserId();

    void setUserId(Long userId);

    @Column(name = "url", nullable = false, length = 1024)
    String getAlbumPhotoUrl();

    void setAlbumPhotoUrl(String albumPhotoUrl);

    @Column(name = "create_time", nullable = false)
    Date getAlbumPhotoCreateTime();

    void setAlbumPhotoCreateTime(Date albumPhotoCreateTime);

    @Column(name = "ref_count", nullable = false)
    Integer getAlbumPhotoRefCount();

    void setAlbumPhotoRefCount(Integer albumPhotoRefCount);

    @Column(name = "disabled", nullable = false)
    Boolean isAlbumPhotoDisabled();

    void setAlbumPhotoDisabled(Boolean albumPhotoDisabled);

}
