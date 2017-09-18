package diary.service.impl;

import beans.BeanManager;
import blog.data.EIResourceLevel;
import blog.repository.IResourceLevelRepo;
import blog.service.exp.TableNotCreateException;
import blog.service.exp.TableNotSaveException;
import diary.data.*;
import diary.repository.*;
import diary.service.IDiaryService;
import diary.view.EIDiaryPageDetail;
import diary.view.EIDiaryPageDraft;
import orm.DatasourceFactory;

import java.sql.Connection;
import java.util.*;

public class DiaryServiceImpl implements IDiaryService {

    private IDiaryBookRepo diaryBookRepo = BeanManager.getInstance().getRepository(IDiaryBookRepo.class);
    private IDiaryPageRepo diaryPageRepo = BeanManager.getInstance().getRepository(IDiaryPageRepo.class);
    private IDiaryMappingRepo diaryMappingRepo = BeanManager.getInstance().getRepository(IDiaryMappingRepo.class);
    private IDiaryMilestoneRepo diaryMilestoneRepo = BeanManager.getInstance().getRepository(IDiaryMilestoneRepo.class);
    private IDiaryPhotoRepo diaryPhotoRepo = BeanManager.getInstance().getRepository(IDiaryPhotoRepo.class);

    private IResourceLevelRepo resourceLevelRepo = BeanManager.getInstance().getRepository(IResourceLevelRepo.class);

    @Override
    public EIDiaryBook saveBook(EIDiaryBook diaryBook) {
        if (null != diaryBook.getDiaryBookId()) {
            EIDiaryBook eiDiaryBook = this.diaryBookRepo.find(diaryBook.getDiaryBookId());
            if (null != eiDiaryBook) {
                eiDiaryBook.copyFrom(diaryBook);
                diaryBook = eiDiaryBook;
            }
        }

        if (null == diaryBook.isDiaryBookDisabled()) {
            diaryBook.setDiaryBookDisabled(false);
        }

        if (null == diaryBook.getDiaryBookPageCount()) {
            diaryBook.setDiaryBookPageCount(0);
        }

        if (!this.diaryBookRepo.save(diaryBook)) {
            throw new TableNotSaveException("diary book");
        }

        return diaryBook;
    }

    @Override
    public EIDiaryPage savePage(EIDiaryPage diaryPage) {
        if (null != diaryPage.getDiaryPageId()) {
            EIDiaryPage eiDiaryPage = this.diaryPageRepo.find(diaryPage.getDiaryPageId());
            if (null != eiDiaryPage) {
                eiDiaryPage.copyFrom(diaryPage);
                diaryPage = eiDiaryPage;
            }
        }

        if (null == diaryPage.isDiaryPageDisabled()) {
            diaryPage.setDiaryPageDisabled(false);
        }

        if (!this.diaryPageRepo.save(diaryPage)) {
            throw new TableNotSaveException("diary page");
        }

        return diaryPage;
    }

    @Override
    public EIDiaryMilestone saveMilestone(EIDiaryMilestone diaryMilestone) {
        if (null != diaryMilestone.getDiaryMilestoneId()) {
            EIDiaryMilestone eiDiaryMilestone = this.diaryMilestoneRepo.find(diaryMilestone.getDiaryMilestoneId());
            if (null != eiDiaryMilestone) {
                eiDiaryMilestone.copyFrom(diaryMilestone);
                diaryMilestone = eiDiaryMilestone;
            }
        }

        if (null == diaryMilestone.isDiaryMilestoneDisabled()) {
            diaryMilestone.setDiaryMilestoneDisabled(false);
        }

        if (!this.diaryMilestoneRepo.save(diaryMilestone)) {
            throw new TableNotSaveException("diary milestone");
        }

        return diaryMilestone;
    }

    @Override
    public EIDiaryPhoto savePhoto(EIDiaryPhoto diaryPhoto) {
        if (null != diaryPhoto.getDiaryPhotoId()) {
            EIDiaryPhoto eiDiaryPhoto = this.diaryPhotoRepo.find(diaryPhoto.getDiaryPhotoId());
            if (null != eiDiaryPhoto) {
                eiDiaryPhoto.copyFrom(diaryPhoto);
                diaryPhoto = eiDiaryPhoto;
            }
        }

        if (null == diaryPhoto.isDiaryPhotoDisabled()) {
            diaryPhoto.setDiaryPhotoDisabled(false);
        }

        if (!this.diaryPhotoRepo.save(diaryPhoto)) {
            throw new TableNotSaveException("diary photo");
        }

        return diaryPhoto;
    }

    @Override
    public Boolean attachPage(Long bookId, Long pageId) {
        DatasourceFactory.begin(Connection.TRANSACTION_SERIALIZABLE);

        EIDiaryBook eiDiaryBook = this.diaryBookRepo.find(bookId);
        if (null == eiDiaryBook) {
            DatasourceFactory.rollback();
            return false;
        }

        EIDiaryMapping byDiaryBookIdAndDiaryPageId = this.diaryMappingRepo.findByDiaryBookIdAndDiaryPageId(bookId, pageId);
        if (null != byDiaryBookIdAndDiaryPageId) {
            if (!byDiaryBookIdAndDiaryPageId.isDiaryMappingDisabled()) {
                return true;
            }

            byDiaryBookIdAndDiaryPageId.setDiaryMappingDisabled(false);
            this.diaryMappingRepo.save(byDiaryBookIdAndDiaryPageId);
        } else {

            EIDiaryMapping eiDiaryMapping = BeanManager.getInstance().createBean(EIDiaryMapping.class);
            eiDiaryMapping.setDiaryBookId(bookId);
            eiDiaryMapping.setDiaryPageId(pageId);
            eiDiaryMapping.setDiaryMappingDisabled(false);
            this.diaryMappingRepo.save(eiDiaryMapping);
        }

        eiDiaryBook.setDiaryBookPageCount(eiDiaryBook.getDiaryBookPageCount() + 1);
        this.diaryBookRepo.save(eiDiaryBook);
        DatasourceFactory.commit();

        return true;
    }

    @Override
    public Boolean releasePage(Long bookId, Long pageId) {
        DatasourceFactory.begin(Connection.TRANSACTION_SERIALIZABLE);

        EIDiaryBook eiDiaryBook = this.diaryBookRepo.find(bookId);
        if (null == eiDiaryBook) {
            DatasourceFactory.rollback();
            return false;
        }

        EIDiaryMapping byDiaryBookIdAndDiaryPageId = this.diaryMappingRepo.findByDiaryBookIdAndDiaryPageId(bookId, pageId);
        if (null == byDiaryBookIdAndDiaryPageId) {
            DatasourceFactory.rollback();
            return false;
        }

        if (!byDiaryBookIdAndDiaryPageId.isDiaryMappingDisabled()) {
            byDiaryBookIdAndDiaryPageId.setDiaryMappingDisabled(true);
            this.diaryMappingRepo.save(byDiaryBookIdAndDiaryPageId);
        }

        eiDiaryBook.setDiaryBookPageCount(eiDiaryBook.getDiaryBookPageCount() - 1);
        this.diaryBookRepo.save(eiDiaryBook);
        DatasourceFactory.commit();

        return true;
    }

    @Override
    public List<EIDiaryBook> listBooks() {
        return this.diaryBookRepo.findAll();
    }

    @Override
    public List<EIDiaryPage> listPages() {
        return this.diaryPageRepo.findAll();
    }

    @Override
    public List<EIDiaryMilestone> listMilestones(Long pageId) {
        return this.diaryMilestoneRepo.findAllByDiaryPageId(pageId);
    }

    @Override
    public List<EIDiaryPhoto> listPhotos(Long pageId) {
        return this.diaryPhotoRepo.findAllByDiaryPageId(pageId);
    }

    @Override
    public List<EIDiaryBook> listBooks(Long userId) {
        if (null == userId) {
            return this.diaryBookRepo.findAllByDiaryBookDisabledAndResourceLevelId(false, 0L);
        }

        List<EIResourceLevel> eiResourceLevels = this.resourceLevelRepo.queryByUserId(userId);

        final StringJoiner levels = new StringJoiner(", ", "(", ")");
        eiResourceLevels.forEach(eiResourceLevel -> {
            levels.add(eiResourceLevel.getResourceLevelId() + "");
        });
        levels.add("0");
        String sql = String.format("Select a From EIDiaryBook a Where a.DiaryBookDisabled != 0 and a.ResourceLevelId in %s", levels.toString());
        return this.diaryBookRepo.queryAllInResourceLevel(sql);
    }

    @Override
    public List<EIDiaryPageDetail> listSimplePages(Long bookId) {
        return transform(this.diaryPageRepo.queryAllSimple(bookId));
    }

    @Override
    public EIDiaryPageDetail getDetailedPage(Long pageId) {
        List<EIDiaryPageDetail> transformed = transform(this.diaryPageRepo.query(pageId));

        if (transformed.isEmpty()) {
            return null;
        }

        return transformed.get(0);
    }

    private List<EIDiaryPageDetail> transform(List<EIDiaryPageDraft> diaryPageDrafts) {
        if (diaryPageDrafts.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Long, EIDiaryPageDetail> ret = new HashMap<>();
        for (EIDiaryPageDraft diaryPageDraft : diaryPageDrafts) {
            EIDiaryPageDetail eiDiaryPageDetail = ret.get(diaryPageDraft.getDiaryPageId());
            if (null == eiDiaryPageDetail) {
                eiDiaryPageDetail = BeanManager.getInstance().createBean(EIDiaryPageDetail.class);
                eiDiaryPageDetail.copyFrom(diaryPageDraft, EIDiaryPage.class);
                ret.put(eiDiaryPageDetail.getDiaryPageId(), eiDiaryPageDetail);
            }

            if (null != diaryPageDraft.getDiaryMilestoneId()) {
                if (null == eiDiaryPageDetail.getDiaryMilestones()) {
                    eiDiaryPageDetail.setDiaryMilestones(new ArrayList<>());
                }

                EIDiaryMilestone eiDiaryMilestone = BeanManager.getInstance().createBean(EIDiaryMilestone.class);
                eiDiaryMilestone.copyFrom(diaryPageDraft, EIDiaryMilestone.class);
                eiDiaryPageDetail.getDiaryMilestones().add(eiDiaryMilestone);
            }

            if (null != diaryPageDraft.getDiaryPhotoId()) {
                if (null == eiDiaryPageDetail.getDiaryPhotoes()) {
                    eiDiaryPageDetail.setDiaryPhotoes(new ArrayList<>());
                }

                EIDiaryPhoto eiDiaryPhoto = BeanManager.getInstance().createBean(EIDiaryPhoto.class);
                eiDiaryPhoto.copyFrom(diaryPageDraft, EIDiaryPhoto.class);
                eiDiaryPageDetail.getDiaryPhotoes().add(eiDiaryPhoto);
            }
        }
        return new ArrayList<>(ret.values());
    }

    @Override
    public void before() {
        if (!this.diaryBookRepo.createTableIfNotExist()) {
            throw new TableNotCreateException("diary book");
        }

        if (!this.diaryMilestoneRepo.createTableIfNotExist()) {
            throw new TableNotCreateException("diary page");
        }

        if (!this.diaryMappingRepo.createTableIfNotExist()) {
            throw new TableNotCreateException("diary mapping");
        }

        if (!this.diaryMilestoneRepo.createTableIfNotExist()) {
            throw new TableNotCreateException("diary milestone");
        }

        if (!this.diaryPhotoRepo.createTableIfNotExist()) {
            throw new TableNotCreateException("diary photo");
        }

        if (!this.resourceLevelRepo.createTableIfNotExist()) {
            throw new TableNotCreateException("resource level");
        }
    }
}
