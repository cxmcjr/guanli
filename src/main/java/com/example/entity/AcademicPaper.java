package com.example.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AcademicPaper {
    private Integer id;
    private String title;
    private LocalDate submitTime;
    private LocalDate acceptTime;
    private String journalName;
    private String keywords;
    private String journalLevel;
    private String authors;
    private String status;
    private String submitter;
    private LocalDateTime createTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public LocalDate getSubmitTime() { return submitTime; }
    public void setSubmitTime(LocalDate submitTime) { this.submitTime = submitTime; }
    public LocalDate getAcceptTime() { return acceptTime; }
    public void setAcceptTime(LocalDate acceptTime) { this.acceptTime = acceptTime; }
    public String getJournalName() { return journalName; }
    public void setJournalName(String journalName) { this.journalName = journalName; }
    public String getKeywords() { return keywords; }
    public void setKeywords(String keywords) { this.keywords = keywords; }
    public String getJournalLevel() { return journalLevel; }
    public void setJournalLevel(String journalLevel) { this.journalLevel = journalLevel; }
    public String getAuthors() { return authors; }
    public void setAuthors(String authors) { this.authors = authors; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSubmitter() { return submitter; }
    public void setSubmitter(String submitter) { this.submitter = submitter; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
