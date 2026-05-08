package com.example.service;

import com.example.dao.AcademicPaperDao;
import com.example.dao.ReviewRecordDao;
import com.example.dao.UploadFileDao;
import com.example.entity.AcademicPaper;
import com.example.entity.ReviewRecord;
import com.example.entity.UploadFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AcademicPaperService {
    private final AcademicPaperDao paperDao;
    private final ReviewRecordDao reviewRecordDao;
    private final UploadFileDao uploadFileDao;

    public AcademicPaperService(AcademicPaperDao paperDao, ReviewRecordDao reviewRecordDao,
            UploadFileDao uploadFileDao) {
        this.paperDao = paperDao;
        this.reviewRecordDao = reviewRecordDao;
        this.uploadFileDao = uploadFileDao;
    }

    public List<AcademicPaper> findAll() {
        return paperDao.findAll();
    }

    public AcademicPaper findById(Integer id) {
        return paperDao.findById(id);
    }

    public int add(AcademicPaper paper) {
        paper.setStatus("pending");
        int id = paperDao.add(paper);
        paper.setId(id);
        return id;
    }

    public int update(AcademicPaper paper) {
        return paperDao.update(paper);
    }

    @Transactional
    public int delete(Integer id) {
        uploadFileDao.deleteByRelated("paper", id);
        return paperDao.delete(id);
    }

    public List<AcademicPaper> findByStatus(String status) {
        return paperDao.findByStatus(status);
    }

    public List<UploadFile> getFiles(Integer id) {
        return uploadFileDao.findByRelated("paper", id);
    }

    public List<ReviewRecord> getReviewRecords(Integer id) {
        return reviewRecordDao.findByAchievement("paper", id);
    }

    @Transactional
    public void review(Integer id, String reviewer, String reviewLevel, String status, String opinion) {
        paperDao.updateStatus(id, status);
        ReviewRecord record = new ReviewRecord();
        record.setAchievementType("paper");
        record.setAchievementId(id);
        record.setReviewer(reviewer);
        record.setReviewLevel(reviewLevel);
        record.setStatus(status);
        record.setOpinion(opinion);
        reviewRecordDao.add(record);
    }

    public int countByLevel(String level) {
        return paperDao.countByLevel(level);
    }

    public int countAll() {
        return paperDao.countAll();
    }
}
