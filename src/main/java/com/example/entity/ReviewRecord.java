package com.example.entity;

import java.time.LocalDateTime;

public class ReviewRecord {
    private Integer id;
    private String achievementType;
    private Integer achievementId;
    private String reviewer;
    private String reviewLevel;
    private String status;
    private String opinion;
    private LocalDateTime reviewTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getAchievementType() { return achievementType; }
    public void setAchievementType(String achievementType) { this.achievementType = achievementType; }
    public Integer getAchievementId() { return achievementId; }
    public void setAchievementId(Integer achievementId) { this.achievementId = achievementId; }
    public String getReviewer() { return reviewer; }
    public void setReviewer(String reviewer) { this.reviewer = reviewer; }
    public String getReviewLevel() { return reviewLevel; }
    public void setReviewLevel(String reviewLevel) { this.reviewLevel = reviewLevel; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getOpinion() { return opinion; }
    public void setOpinion(String opinion) { this.opinion = opinion; }
    public LocalDateTime getReviewTime() { return reviewTime; }
    public void setReviewTime(LocalDateTime reviewTime) { this.reviewTime = reviewTime; }
}
