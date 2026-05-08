package com.example.dao;

import com.example.entity.InnovationProject;
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
public class InnovationProjectDao {
    private final JdbcTemplate jdbcTemplate;

    public InnovationProjectDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<InnovationProject> findAll() {
        return jdbcTemplate.query("SELECT * FROM innovation_project ORDER BY create_time DESC",
                new BeanPropertyRowMapper<>(InnovationProject.class));
    }

    public InnovationProject findById(Integer id) {
        return jdbcTemplate.queryForObject("SELECT * FROM innovation_project WHERE id=?",
                new BeanPropertyRowMapper<>(InnovationProject.class), id);
    }

    public int add(InnovationProject p) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO innovation_project(project_name,level,type,teacher,members,start_date,status,submitter) VALUES(?,?,?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, p.getProjectName());
            ps.setString(2, p.getLevel());
            ps.setString(3, p.getType());
            ps.setString(4, p.getTeacher());
            ps.setString(5, p.getMembers());
            ps.setObject(6, p.getStartDate());
            ps.setString(7, p.getStatus());
            ps.setString(8, p.getSubmitter());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public int update(InnovationProject p) {
        return jdbcTemplate.update(
                "UPDATE innovation_project SET project_name=?,level=?,type=?,teacher=?,members=?,start_date=? WHERE id=?",
                p.getProjectName(), p.getLevel(), p.getType(), p.getTeacher(),
                p.getMembers(), p.getStartDate(), p.getId());
    }

    public int updateStatus(Integer id, String status) {
        return jdbcTemplate.update("UPDATE innovation_project SET status=? WHERE id=?", status, id);
    }

    public int delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM innovation_project WHERE id=?", id);
    }

    public List<InnovationProject> findByStatus(String status) {
        return jdbcTemplate.query("SELECT * FROM innovation_project WHERE status=? ORDER BY create_time DESC",
                new BeanPropertyRowMapper<>(InnovationProject.class), status);
    }

    public int countByLevel(String level) {
        Integer result = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM innovation_project WHERE level=?", Integer.class, level);
        return result != null ? result : 0;
    }

    public int countByType(String type) {
        Integer result = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM innovation_project WHERE type=?", Integer.class, type);
        return result != null ? result : 0;
    }

    public int countAll() {
        Integer result = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM innovation_project", Integer.class);
        return result != null ? result : 0;
    }
}
