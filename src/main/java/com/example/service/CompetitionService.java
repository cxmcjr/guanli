package com.example.service;

import com.example.dao.CompetitionDao;
import com.example.dao.ReviewRecordDao;
import com.example.dao.UploadFileDao;
import com.example.entity.Competition;
import com.example.entity.ReviewRecord;
import com.example.entity.UploadFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CompetitionService {
    private final CompetitionDao competitionDao;
    private final ReviewRecordDao reviewRecordDao;
    private final UploadFileDao uploadFileDao;

    public CompetitionService(CompetitionDao competitionDao, ReviewRecordDao reviewRecordDao,
            UploadFileDao uploadFileDao) {
        this.competitionDao = competitionDao;
        this.reviewRecordDao = reviewRecordDao;
        this.uploadFileDao = uploadFileDao;
    }

    public List<Competition> findAll() {
        return competitionDao.findAll();
    }

    public Competition findById(Integer id) {
        return competitionDao.findById(id);
    }

    public int add(Competition competition) {
        competition.setStatus("pending");
        int id = competitionDao.add(competition);
        competition.setId(id);
        return id;
    }

    public int update(Competition competition) {
        return competitionDao.update(competition);
    }

    @Transactional
    public int delete(Integer id) {
        uploadFileDao.deleteByRelated("competition", id);
        return competitionDao.delete(id);
    }

    public List<Competition> findByStatus(String status) {
        return competitionDao.findByStatus(status);
    }

    public List<UploadFile> getFiles(Integer id) {
        return uploadFileDao.findByRelated("competition", id);
    }

    public List<ReviewRecord> getReviewRecords(Integer id) {
        return reviewRecordDao.findByAchievement("competition", id);
    }

    @Transactional
    public void review(Integer id, String reviewer, String reviewLevel, String status, String opinion) {
        competitionDao.updateStatus(id, status);
        ReviewRecord record = new ReviewRecord();
        record.setAchievementType("competition");
        record.setAchievementId(id);
        record.setReviewer(reviewer);
        record.setReviewLevel(reviewLevel);
        record.setStatus(status);
        record.setOpinion(opinion);
        reviewRecordDao.add(record);
    }

    public int countByCategory(String category) {
        return competitionDao.countByCategory(category);
    }

    public int countByLevel(String level) {
        return competitionDao.countByLevel(level);
    }

    public int countAll() {
        return competitionDao.countAll();
    }
}
