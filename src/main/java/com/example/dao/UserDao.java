package com.example.dao;

import com.example.entity.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User findByUsername(String username) {
        List<User> list = jdbcTemplate.query(
                "SELECT * FROM users WHERE username=?",
                new BeanPropertyRowMapper<>(User.class), username);
        return list.isEmpty() ? null : list.get(0);
    }

    public User findById(Integer id) {
        List<User> list = jdbcTemplate.query(
                "SELECT * FROM users WHERE id=?",
                new BeanPropertyRowMapper<>(User.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users ORDER BY create_time DESC",
                new BeanPropertyRowMapper<>(User.class));
    }

    public int add(User user) {
        return jdbcTemplate.update(
                "INSERT INTO users(username,password,real_name,role) VALUES(?,?,?,?)",
                user.getUsername(), user.getPassword(), user.getRealName(), user.getRole());
    }

    public int update(User user) {
        return jdbcTemplate.update(
                "UPDATE users SET real_name=?,role=? WHERE id=?",
                user.getRealName(), user.getRole(), user.getId());
    }

    public int updatePassword(Integer id, String password) {
        return jdbcTemplate.update("UPDATE users SET password=? WHERE id=?", password, id);
    }

    public int delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id);
    }

    public boolean existsByUsername(String username) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE username=?", Integer.class, username);
        return count != null && count > 0;
    }
}
