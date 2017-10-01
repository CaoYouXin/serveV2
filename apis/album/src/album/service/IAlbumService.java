package album.service;

import album.data.EIAlbumAlbum;
import album.data.EIAlbumPhoto;
import album.view.EIPagedPhotos;
import rest.Service;

import java.util.List;

public interface IAlbumService extends Service {

    List<EIAlbumAlbum> listPublicAlbumVideo();

    List<EIAlbumAlbum> listAlbum(Long userId);

    EIPagedPhotos listPhoto(Long userId, Integer page, Integer size);

    EIAlbumAlbum saveAlbum(EIAlbumAlbum album);

    EIAlbumPhoto savePhoto(EIAlbumPhoto photo);

    Boolean attachAlbum(Long albumId, Long photoId);

    Boolean releasePhoto(Long albumId, Long photoId);

}
