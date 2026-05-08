package com.example.service;

import com.example.dao.InnovationProjectDao;
import com.example.dao.ReviewRecordDao;
import com.example.dao.UploadFileDao;
import com.example.entity.InnovationProject;
import com.example.entity.ReviewRecord;
import com.example.entity.UploadFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class InnovationProjectService {
    private final InnovationProjectDao projectDao;
    private final ReviewRecordDao reviewRecordDao;
    private final UploadFileDao uploadFileDao;

    public InnovationProjectService(InnovationProjectDao projectDao, ReviewRecordDao reviewRecordDao,
            UploadFileDao uploadFileDao) {
        this.projectDao = projectDao;
        this.reviewRecordDao = reviewRecordDao;
        this.uploadFileDao = uploadFileDao;
    }

    public List<InnovationProject> findAll() {
        return projectDao.findAll();
    }

    public InnovationProject findById(Integer id) {
        return projectDao.findById(id);
    }

    public int add(InnovationProject project) {
        project.setStatus("pending");
        int id = projectDao.add(project);
        project.setId(id);
        return id;
    }

    public int update(InnovationProject project) {
        return projectDao.update(project);
    }

    @Transactional
    public int delete(Integer id) {
        uploadFileDao.deleteByRelated("innovation", id);
        return projectDao.delete(id);
    }

    public List<InnovationProject> findByStatus(String status) {
        return projectDao.findByStatus(status);
    }

    public List<UploadFile> getFiles(Integer id) {
        return uploadFileDao.findByRelated("innovation", id);
    }

    public List<ReviewRecord> getReviewRecords(Integer id) {
        return reviewRecordDao.findByAchievement("innovation", id);
    }

    @Transactional
    public void review(Integer id, String reviewer, String reviewLevel, String status, String opinion) {
        projectDao.updateStatus(id, status);
        ReviewRecord record = new ReviewRecord();
        record.setAchievementType("innovation");
        record.setAchievementId(id);
        record.setReviewer(reviewer);
        record.setReviewLevel(reviewLevel);
        record.setStatus(status);
        record.setOpinion(opinion);
        reviewRecordDao.add(record);
    }

    public int countByLevel(String level) {
        return projectDao.countByLevel(level);
    }

    public int countByType(String type) {
        return projectDao.countByType(type);
    }

    public int countAll() {
        return projectDao.countAll();
    }
}
