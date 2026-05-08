package com.example.dao;

import com.example.entity.Competition;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class CompetitionDao {
    private final JdbcTemplate jdbcTemplate;

    public CompetitionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Competition> findAll() {
        return jdbcTemplate.query("SELECT * FROM competition ORDER BY create_time DESC",
                new BeanPropertyRowMapper<>(Competition.class));
    }

    public Competition findById(Integer id) {
        return jdbcTemplate.queryForObject("SELECT * FROM competition WHERE id=?",
                new BeanPropertyRowMapper<>(Competition.class), id);
    }

    public int add(Competition c) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO competition(category,name,award_level,award_grade,award_unit,host_unit,organizer,award_date,teacher,participants,status,submitter) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, c.getCategory());
            ps.setString(2, c.getName());
            ps.setString(3, c.getAwardLevel());
            ps.setString(4, c.getAwardGrade());
            ps.setString(5, c.getAwardUnit());
            ps.setString(6, c.getHostUnit());
            ps.setString(7, c.getOrganizer());
            ps.setObject(8, c.getAwardDate());
            ps.setString(9, c.getTeacher());
            ps.setString(10, c.getParticipants());
            ps.setString(11, c.getStatus());
            ps.setString(12, c.getSubmitter());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public int update(Competition c) {
        return jdbcTemplate.update(
                "UPDATE competition SET category=?,name=?,award_level=?,award_grade=?,award_unit=?,host_unit=?,organizer=?,award_date=?,teacher=?,participants=? WHERE id=?",
                c.getCategory(), c.getName(), c.getAwardLevel(), c.getAwardGrade(),
                c.getAwardUnit(), c.getHostUnit(), c.getOrganizer(), c.getAwardDate(),
                c.getTeacher(), c.getParticipants(), c.getId());
    }

    public int updateStatus(Integer id, String status) {
        return jdbcTemplate.update("UPDATE competition SET status=? WHERE id=?", status, id);
    }

    public int delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM competition WHERE id=?", id);
    }

    public List<Competition> findByStatus(String status) {
        return jdbcTemplate.query("SELECT * FROM competition WHERE status=? ORDER BY create_time DESC",
                new BeanPropertyRowMapper<>(Competition.class), status);
    }

    public int countByCategory(String category) {
        Integer result = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM competition WHERE category=?", Integer.class, category);
        return result != null ? result : 0;
    }

    public int countByLevel(String level) {
        Integer result = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM competition WHERE award_level=?", Integer.class, level);
        return result != null ? result : 0;
    }

    public int countAll() {
        Integer result = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM competition", Integer.class);
        return result != null ? result : 0;
    }
}
