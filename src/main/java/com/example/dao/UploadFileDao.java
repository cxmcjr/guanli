package com.example.dao;

import com.example.entity.UploadFile;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class UploadFileDao {
    private final JdbcTemplate jdbcTemplate;

    public UploadFileDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<UploadFile> findAll() {
        return jdbcTemplate.query("SELECT * FROM upload_file ORDER BY upload_time DESC",
                new BeanPropertyRowMapper<>(UploadFile.class));
    }

    public UploadFile findById(Integer id) {
        return jdbcTemplate.queryForObject("SELECT * FROM upload_file WHERE id=?",
                new BeanPropertyRowMapper<>(UploadFile.class), id);
    }

    public List<UploadFile> findByRelated(String relatedType, Integer relatedId) {
        return jdbcTemplate.query(
                "SELECT * FROM upload_file WHERE related_type=? AND related_id=? ORDER BY upload_time DESC",
                new BeanPropertyRowMapper<>(UploadFile.class), relatedType, relatedId);
    }

    public List<UploadFile> findByCategory(String category) {
        return jdbcTemplate.query("SELECT * FROM upload_file WHERE category=? ORDER BY upload_time DESC",
                new BeanPropertyRowMapper<>(UploadFile.class), category);
    }

    public int add(UploadFile f) {
        return jdbcTemplate.update(
                "INSERT INTO upload_file(file_name,original_name,file_path,file_size,file_type,category,related_type,related_id) VALUES(?,?,?,?,?,?,?,?)",
                f.getFileName(), f.getOriginalName(), f.getFilePath(), f.getFileSize(),
                f.getFileType(), f.getCategory(), f.getRelatedType(), f.getRelatedId());
    }

    public int delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM upload_file WHERE id=?", id);
    }

    public int deleteByRelated(String relatedType, Integer relatedId) {
        return jdbcTemplate.update("DELETE FROM upload_file WHERE related_type=? AND related_id=?", relatedType, relatedId);
    }
}
