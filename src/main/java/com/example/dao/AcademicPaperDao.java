package com.example.dao;

import com.example.entity.AcademicPaper;
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
public class AcademicPaperDao {
    private final JdbcTemplate jdbcTemplate;

    public AcademicPaperDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<AcademicPaper> findAll() {
        return jdbcTemplate.query("SELECT * FROM academic_paper ORDER BY create_time DESC",
                new BeanPropertyRowMapper<>(AcademicPaper.class));
    }

    public AcademicPaper findById(Integer id) {
        return jdbcTemplate.queryForObject("SELECT * FROM academic_paper WHERE id=?",
                new BeanPropertyRowMapper<>(AcademicPaper.class), id);
    }

    public int add(AcademicPaper p) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO academic_paper(title,submit_time,accept_time,journal_name,keywords,journal_level,authors,status,submitter) VALUES(?,?,?,?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, p.getTitle());
            ps.setObject(2, p.getSubmitTime());
            ps.setObject(3, p.getAcceptTime());
            ps.setString(4, p.getJournalName());
            ps.setString(5, p.getKeywords());
            ps.setString(6, p.getJournalLevel());
            ps.setString(7, p.getAuthors());
            ps.setString(8, p.getStatus());
            ps.setString(9, p.getSubmitter());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public int update(AcademicPaper p) {
        return jdbcTemplate.update(
                "UPDATE academic_paper SET title=?,submit_time=?,accept_time=?,journal_name=?,keywords=?,journal_level=?,authors=? WHERE id=?",
                p.getTitle(), p.getSubmitTime(), p.getAcceptTime(), p.getJournalName(),
                p.getKeywords(), p.getJournalLevel(), p.getAuthors(), p.getId());
    }

    public int updateStatus(Integer id, String status) {
        return jdbcTemplate.update("UPDATE academic_paper SET status=? WHERE id=?", status, id);
    }

    public int delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM academic_paper WHERE id=?", id);
    }

    public List<AcademicPaper> findByStatus(String status) {
        return jdbcTemplate.query("SELECT * FROM academic_paper WHERE status=? ORDER BY create_time DESC",
                new BeanPropertyRowMapper<>(AcademicPaper.class), status);
    }

    public int countByLevel(String level) {
        Integer result = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM academic_paper WHERE journal_level=?", Integer.class, level);
        return result != null ? result : 0;
    }

    public int countAll() {
        Integer result = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM academic_paper", Integer.class);
        return result != null ? result : 0;
    }
}
