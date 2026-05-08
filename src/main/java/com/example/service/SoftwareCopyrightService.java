package com.example.service;

import com.example.dao.SoftwareCopyrightDao;
import com.example.dao.ReviewRecordDao;
import com.example.dao.UploadFileDao;
import com.example.entity.SoftwareCopyright;
import com.example.entity.ReviewRecord;
import com.example.entity.UploadFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class SoftwareCopyrightService {
    private final SoftwareCopyrightDao copyrightDao;
    private final ReviewRecordDao reviewRecordDao;
    private final UploadFileDao uploadFileDao;

    public SoftwareCopyrightService(SoftwareCopyrightDao copyrightDao, ReviewRecordDao reviewRecordDao,
            UploadFileDao uploadFileDao) {
        this.copyrightDao = copyrightDao;
        this.reviewRecordDao = reviewRecordDao;
        this.uploadFileDao = uploadFileDao;
    }

    public List<SoftwareCopyright> findAll() {
        return copyrightDao.findAll();
    }

    public SoftwareCopyright findById(Integer id) {
        return copyrightDao.findById(id);
    }

    public int add(SoftwareCopyright copyright) {
        copyright.setStatus("pending");
        int id = copyrightDao.add(copyright);
        copyright.setId(id);
        return id;
    }

    public int update(SoftwareCopyright copyright) {
        return copyrightDao.update(copyright);
    }

    @Transactional
    public int delete(Integer id) {
        uploadFileDao.deleteByRelated("software", id);
        return copyrightDao.delete(id);
    }

    public List<SoftwareCopyright> findByStatus(String status) {
        return copyrightDao.findByStatus(status);
    }

    public List<UploadFile> getFiles(Integer id) {
        return uploadFileDao.findByRelated("software", id);
    }

    public List<ReviewRecord> getReviewRecords(Integer id) {
        return reviewRecordDao.findByAchievement("software", id);
    }

    @Transactional
    public void review(Integer id, String reviewer, String reviewLevel, String status, String opinion) {
        copyrightDao.updateStatus(id, status);
        ReviewRecord record = new ReviewRecord();
        record.setAchievementType("software");
        record.setAchievementId(id);
        record.setReviewer(reviewer);
        record.setReviewLevel(reviewLevel);
        record.setStatus(status);
        record.setOpinion(opinion);
        reviewRecordDao.add(record);
    }

    public int countAll() {
        return copyrightDao.countAll();
    }
}
