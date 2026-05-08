package com.example;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class StudentDao {
    private final JdbcTemplate jdbcTemplate;

    public StudentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Student> findAll() {
        return jdbcTemplate.query("SELECT * FROM student", new BeanPropertyRowMapper<>(Student.class));
    }

    public void add(Student s) {
        jdbcTemplate.update("INSERT INTO student(stu_id,name,gender,age,major,phone) VALUES(?,?,?,?,?,?)",
                s.getStuId(), s.getName(), s.getGender(), s.getAge(), s.getMajor(), s.getPhone());
    }

    public Student findByStuId(String stuId) {
        return jdbcTemplate.queryForObject("SELECT * FROM student WHERE stu_id=?",
                new BeanPropertyRowMapper<>(Student.class), stuId);
    }

    public void update(Student s) {
        jdbcTemplate.update("UPDATE student SET name=?,gender=?,age=?,major=?,phone=? WHERE stu_id=?",
                s.getName(), s.getGender(), s.getAge(), s.getMajor(), s.getPhone(), s.getStuId());
    }

    public void delete(String stuId) {
        jdbcTemplate.update("DELETE FROM student WHERE stu_id=?", stuId);
    }
}