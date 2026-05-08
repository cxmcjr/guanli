package com.example.dao;

import com.example.entity.SoftwareCopyright;
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
public class SoftwareCopyrightDao {
    private final JdbcTemplate jdbcTemplate;

    public SoftwareCopyrightDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SoftwareCopyright> findAll() {
        return jdbcTemplate.query("SELECT * FROM software_copyright ORDER BY create_time DESC",
                new BeanPropertyRowMapper<>(SoftwareCopyright.class));
    }

    public SoftwareCopyright findById(Integer id) {
        return jdbcTemplate.queryForObject("SELECT * FROM software_copyright WHERE id=?",
                new BeanPropertyRowMapper<>(SoftwareCopyright.class), id);
    }

    public int add(SoftwareCopyright s) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO software_copyright(name,unit,author,registration_no,registration_date,status,submitter) VALUES(?,?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, s.getName());
            ps.setString(2, s.getUnit());
            ps.setString(3, s.getAuthor());
            ps.setString(4, s.getRegistrationNo());
            ps.setObject(5, s.getRegistrationDate());
            ps.setString(6, s.getStatus());
            ps.setString(7, s.getSubmitter());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public int update(SoftwareCopyright s) {
        return jdbcTemplate.update(
                "UPDATE software_copyright SET name=?,unit=?,author=?,registration_no=?,registration_date=? WHERE id=?",
                s.getName(), s.getUnit(), s.getAuthor(), s.getRegistrationNo(),
                s.getRegistrationDate(), s.getId());
    }

    public int updateStatus(Integer id, String status) {
        return jdbcTemplate.update("UPDATE software_copyright SET status=? WHERE id=?", status, id);
    }

    public int delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM software_copyright WHERE id=?", id);
    }

    public List<SoftwareCopyright> findByStatus(String status) {
        return jdbcTemplate.query("SELECT * FROM software_copyright WHERE status=? ORDER BY create_time DESC",
                new BeanPropertyRowMapper<>(SoftwareCopyright.class), status);
    }

    public int countAll() {
        Integer result = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM software_copyright", Integer.class);
        return result != null ? result : 0;
    }
}
