package album.data;

import beans.EntityBeanI;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "album_album")
public interface EIAlbumAlbum extends EntityBeanI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getAlbumId();

    void setAlbumId(Long albumId);

    @Column(name = "user_id", nullable = false)
    Long getUserId();

    void setUserId(Long userId);

    @Column(name = "name", nullable = false)
    String getAlbumName();

    void setAlbumName(String albumName);

    @Column(name = "create_time", nullable = false)
    Date getAlbumCreateTime();

    void setAlbumCreateTime(Date albumCreateTime);

    @Column(name = "photo_count", nullable = false)
    Integer getAlbumPhotoCount();

    void setAlbumPhotoCount(Integer albumPhotoCount);

    @Column(name = "video_url", length = 1024)
    String getAlbumVideoUrl();

    void setAlbumVideoUrl(String albumVideoUrl);

    @Column(name = "video_create_time")
    Date getAlbumVideoCreateTime();

    void setAlbumVideoCreateTime(Date albumVideoCreateTime);

    @Column(name = "video_public")
    Boolean isAlbumVideoPublic();

    void setAlbumVideoPublic(Boolean albumVideoPublic);

    @Column(name = "disabled", nullable = false)
    Boolean isAlbumDisabled();

    void setAlbumDisabled(Boolean albumDisabled);

}
