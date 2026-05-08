package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class Main implements CommandLineRunner {
    private final JdbcTemplate jdbcTemplate;

    public Main(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("✅ 系统启动成功，访问：http://localhost:8080");
    }

    @Override
    public void run(String... args) {
        // 自动建库建表
        jdbcTemplate.execute("CREATE DATABASE IF NOT EXISTS student_db");
        jdbcTemplate.execute("USE student_db");
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS student (" +
                        "id INT PRIMARY KEY AUTO_INCREMENT," +
                        "stu_id VARCHAR(30) NOT NULL UNIQUE," +
                        "name VARCHAR(50) NOT NULL," +
                        "gender VARCHAR(10)," +
                        "age INT," +
                        "major VARCHAR(100)," +
                        "phone VARCHAR(20)" +
                        ")"
        );
    }
}