package com.example.dao;

import com.example.entity.ReviewRecord;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ReviewRecordDao {
    private final JdbcTemplate jdbcTemplate;

    public ReviewRecordDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ReviewRecord> findByAchievement(String achievementType, Integer achievementId) {
        return jdbcTemplate.query(
                "SELECT * FROM review_record WHERE achievement_type=? AND achievement_id=? ORDER BY review_time DESC",
                new BeanPropertyRowMapper<>(ReviewRecord.class), achievementType, achievementId);
    }

    public int add(ReviewRecord r) {
        return jdbcTemplate.update(
                "INSERT INTO review_record(achievement_type,achievement_id,reviewer,review_level,status,opinion) VALUES(?,?,?,?,?,?)",
                r.getAchievementType(), r.getAchievementId(), r.getReviewer(),
                r.getReviewLevel(), r.getStatus(), r.getOpinion());
    }

    public List<ReviewRecord> findAll() {
        return jdbcTemplate.query("SELECT * FROM review_record ORDER BY review_time DESC",
                new BeanPropertyRowMapper<>(ReviewRecord.class));
    }
}
