package album.service.impl;

import album.data.EIAlbumAlbum;
import album.data.EIAlbumMapping;
import album.data.EIAlbumPhoto;
import album.repository.IAlbumAlbumRepo;
import album.repository.IAlbumMappingRepo;
import album.repository.IAlbumPhotoRepo;
import album.service.IAlbumService;
import album.view.EIPagedPhotos;
import beans.BeanManager;
import blog.service.exp.TableNotCreateException;
import blog.service.exp.TableNotSaveException;
import orm.DatasourceFactory;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

public class AlbumServiceImpl implements IAlbumService {

    private IAlbumAlbumRepo albumRepo = BeanManager.getInstance().getRepository(IAlbumAlbumRepo.class);
    private IAlbumPhotoRepo albumPhotoRepo = BeanManager.getInstance().getRepository(IAlbumPhotoRepo.class);
    private IAlbumMappingRepo albumMappingRepo = BeanManager.getInstance().getRepository(IAlbumMappingRepo.class);

    @Override
    public void before() {
        if (!albumRepo.createTableIfNotExist()) {
            throw new TableNotCreateException("album album");
        }

        if (!albumPhotoRepo.createTableIfNotExist()) {
            throw new TableNotCreateException("album photo");
        }

        if (!albumMappingRepo.createTableIfNotExist()) {
            throw new TableNotCreateException("album mapping");
        }
    }

    @Override
    public List<EIAlbumAlbum> listPublicAlbumVideo() {
        return this.albumRepo.findAllByAlbumVideoPublicAndAlbumDisabled(true, false);
    }

    @Override
    public List<EIAlbumAlbum> listAlbum(Long userId) {
        return this.albumRepo.findAllByUserIdAndAlbumDisabled(userId, false);
    }

    @Override
    public EIPagedPhotos listPhoto(Long userId, Integer page, Integer size) {
        EIPagedPhotos ret = this.albumPhotoRepo.queryAllByUserId(userId);

        ret.setPhotos(
                this.albumPhotoRepo.queryAllByUserId(userId, (long) ((page - 1) * size), size)
        );

        return ret;
    }

    @Override
    public List<EIAlbumMapping> listAlbumPhotoIds(Long albumId) {
        return this.albumMappingRepo.findAllByAlbumIdAndAlbumMappingDisabled(albumId, false);
    }

    @Override
    public EIPagedPhotos listAlbumPhoto(Long albumId, Integer page, Integer size) {
        EIPagedPhotos ret = this.albumMappingRepo.queryAllByAlbumId(albumId);

        ret.setPhotos(
                this.albumPhotoRepo.queryAllByAlbumId(albumId, (long) ((page - 1) * size), size)
        );

        return ret;
    }

    @Override
    public EIAlbumAlbum saveAlbum(EIAlbumAlbum album) {
        if (null != album.getAlbumId()) {
            EIAlbumAlbum eiAlbumAlbum = this.albumRepo.find(album.getAlbumId());
            eiAlbumAlbum.copyFrom(album);
            album = eiAlbumAlbum;
        }

        if (null == album.getAlbumCreateTime()) {
            album.setAlbumCreateTime(new Date());
        }

        if (null == album.getAlbumPhotoCount()) {
            album.setAlbumPhotoCount(0);
        }

        if (null == album.isAlbumDisabled()) {
            album.setAlbumDisabled(false);
        }

        if (!this.albumRepo.save(album)) {
            throw new TableNotSaveException("album album");
        }

        return album;
    }

    @Override
    public EIAlbumPhoto savePhoto(EIAlbumPhoto photo) {
        if (null != photo.getAlbumPhotoId()) {
            EIAlbumPhoto eiAlbumPhoto = this.albumPhotoRepo.find(photo.getAlbumPhotoId());
            eiAlbumPhoto.copyFrom(photo);
            photo = eiAlbumPhoto;
        }

        if (null == photo.getAlbumPhotoCreateTime()) {
            photo.setAlbumPhotoCreateTime(new Date());
        }

        if (null == photo.getAlbumPhotoRefCount()) {
            photo.setAlbumPhotoRefCount(0);
        }

        if (null == photo.isAlbumPhotoDisabled()) {
            photo.setAlbumPhotoDisabled(false);
        }

        if (!this.albumPhotoRepo.save(photo)) {
            throw new TableNotSaveException("album photo");
        }

        return photo;
    }

    @Override
    public Boolean attachAlbum(Long albumId, Long photoId) {
        DatasourceFactory.begin(Connection.TRANSACTION_SERIALIZABLE);

        EIAlbumAlbum eiAlbumAlbum = this.albumRepo.find(albumId);
        EIAlbumPhoto eiAlbumPhoto = this.albumPhotoRepo.find(photoId);

        EIAlbumMapping byAlbumIdAndAlbumPhotoId = this.albumMappingRepo.findByAlbumIdAndAlbumPhotoId(albumId, photoId);
        if (null == eiAlbumAlbum || null == eiAlbumPhoto || (null != byAlbumIdAndAlbumPhotoId && !byAlbumIdAndAlbumPhotoId.isAlbumMappingDisabled())) {
            DatasourceFactory.rollback();
            return false;
        }

        if (null == byAlbumIdAndAlbumPhotoId) {
            byAlbumIdAndAlbumPhotoId = BeanManager.getInstance().createBean(EIAlbumMapping.class);
            byAlbumIdAndAlbumPhotoId.setAlbumId(albumId);
            byAlbumIdAndAlbumPhotoId.setAlbumPhotoId(photoId);
        }
        byAlbumIdAndAlbumPhotoId.setAlbumMappingDisabled(false);

        eiAlbumAlbum.setAlbumPhotoCount(eiAlbumAlbum.getAlbumPhotoCount() + 1);
        eiAlbumPhoto.setAlbumPhotoRefCount(eiAlbumPhoto.getAlbumPhotoRefCount() + 1);

        if (!(this.albumMappingRepo.save(byAlbumIdAndAlbumPhotoId) && this.albumRepo.save(eiAlbumAlbum) && this.albumPhotoRepo.save(eiAlbumPhoto))) {
            DatasourceFactory.rollback();
            return false;
        }

        DatasourceFactory.commit();
        return true;
    }

    @Override
    public Boolean releasePhoto(Long albumId, Long photoId) {
        DatasourceFactory.begin(Connection.TRANSACTION_SERIALIZABLE);

        EIAlbumAlbum eiAlbumAlbum = this.albumRepo.find(albumId);
        EIAlbumPhoto eiAlbumPhoto = this.albumPhotoRepo.find(photoId);

        EIAlbumMapping byAlbumIdAndAlbumPhotoId = this.albumMappingRepo.findByAlbumIdAndAlbumPhotoId(albumId, photoId);
        if (null == eiAlbumAlbum || null == eiAlbumPhoto || null == byAlbumIdAndAlbumPhotoId || byAlbumIdAndAlbumPhotoId.isAlbumMappingDisabled()) {
            DatasourceFactory.rollback();
            return false;
        }

        byAlbumIdAndAlbumPhotoId.setAlbumMappingDisabled(true);

        eiAlbumAlbum.setAlbumPhotoCount(eiAlbumAlbum.getAlbumPhotoCount() - 1);
        eiAlbumPhoto.setAlbumPhotoRefCount(eiAlbumPhoto.getAlbumPhotoRefCount() - 1);

        if (!(this.albumMappingRepo.save(byAlbumIdAndAlbumPhotoId) && this.albumRepo.save(eiAlbumAlbum) && this.albumPhotoRepo.save(eiAlbumPhoto))) {
            DatasourceFactory.rollback();
            return false;
        }

        DatasourceFactory.commit();
        return true;
    }

}
