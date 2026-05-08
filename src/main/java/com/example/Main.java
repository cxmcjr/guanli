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
                System.out.println("系统启动成功，访问：http://localhost:8080");
        }

        @Override
        public void run(String... args) {
                jdbcTemplate.execute("CREATE DATABASE IF NOT EXISTS stucompetition");
                jdbcTemplate.execute("USE stucompetition");

                jdbcTemplate.execute(
                                "CREATE TABLE IF NOT EXISTS student (" +
                                                "id INT PRIMARY KEY AUTO_INCREMENT," +
                                                "stu_id VARCHAR(30) NOT NULL UNIQUE," +
                                                "name VARCHAR(50) NOT NULL," +
                                                "gender VARCHAR(10)," +
                                                "age INT," +
                                                "major VARCHAR(100)," +
                                                "phone VARCHAR(20)" +
                                                ")");

                jdbcTemplate.execute(
                                "CREATE TABLE IF NOT EXISTS users (" +
                                                "id INT PRIMARY KEY AUTO_INCREMENT," +
                                                "username VARCHAR(50) NOT NULL UNIQUE," +
                                                "password VARCHAR(100) NOT NULL," +
                                                "real_name VARCHAR(50)," +
                                                "role VARCHAR(20) NOT NULL DEFAULT 'student'," +
                                                "create_time DATETIME DEFAULT CURRENT_TIMESTAMP" +
                                                ")");

                jdbcTemplate.execute(
                                "CREATE TABLE IF NOT EXISTS competition (" +
                                                "id INT PRIMARY KEY AUTO_INCREMENT," +
                                                "category VARCHAR(10) NOT NULL," +
                                                "name VARCHAR(200) NOT NULL," +
                                                "award_level VARCHAR(20) NOT NULL," +
                                                "award_grade VARCHAR(20) NOT NULL," +
                                                "award_unit VARCHAR(200)," +
                                                "host_unit VARCHAR(200)," +
                                                "organizer VARCHAR(200)," +
                                                "award_date DATE," +
                                                "teacher VARCHAR(100)," +
                                                "participants TEXT," +
                                                "status VARCHAR(20) DEFAULT 'pending'," +
                                                "submitter VARCHAR(50)," +
                                                "create_time DATETIME DEFAULT CURRENT_TIMESTAMP" +
                                                ")");

                jdbcTemplate.execute(
                                "CREATE TABLE IF NOT EXISTS innovation_project (" +
                                                "id INT PRIMARY KEY AUTO_INCREMENT," +
                                                "project_name VARCHAR(200) NOT NULL," +
                                                "level VARCHAR(20) NOT NULL," +
                                                "type VARCHAR(30) NOT NULL," +
                                                "teacher VARCHAR(100)," +
                                                "members TEXT," +
                                                "start_date DATE," +
                                                "status VARCHAR(20) DEFAULT 'pending'," +
                                                "submitter VARCHAR(50)," +
                                                "create_time DATETIME DEFAULT CURRENT_TIMESTAMP" +
                                                ")");

                jdbcTemplate.execute(
                                "CREATE TABLE IF NOT EXISTS software_copyright (" +
                                                "id INT PRIMARY KEY AUTO_INCREMENT," +
                                                "name VARCHAR(200) NOT NULL," +
                                                "unit VARCHAR(100)," +
                                                "author VARCHAR(100)," +
                                                "registration_no VARCHAR(50) NOT NULL," +
                                                "registration_date DATE," +
                                                "status VARCHAR(20) DEFAULT 'pending'," +
                                                "submitter VARCHAR(50)," +
                                                "create_time DATETIME DEFAULT CURRENT_TIMESTAMP" +
                                                ")");

                jdbcTemplate.execute(
                                "CREATE TABLE IF NOT EXISTS academic_paper (" +
                                                "id INT PRIMARY KEY AUTO_INCREMENT," +
                                                "title VARCHAR(300) NOT NULL," +
                                                "submit_time DATE," +
                                                "accept_time DATE," +
                                                "journal_name VARCHAR(200)," +
                                                "keywords VARCHAR(300)," +
                                                "journal_level VARCHAR(50)," +
                                                "authors VARCHAR(300)," +
                                                "status VARCHAR(20) DEFAULT 'pending'," +
                                                "submitter VARCHAR(50)," +
                                                "create_time DATETIME DEFAULT CURRENT_TIMESTAMP" +
                                                ")");

                jdbcTemplate.execute(
                                "CREATE TABLE IF NOT EXISTS review_record (" +
                                                "id INT PRIMARY KEY AUTO_INCREMENT," +
                                                "achievement_type VARCHAR(30) NOT NULL," +
                                                "achievement_id INT NOT NULL," +
                                                "reviewer VARCHAR(50)," +
                                                "review_level VARCHAR(20) NOT NULL," +
                                                "status VARCHAR(20) NOT NULL," +
                                                "opinion TEXT," +
                                                "review_time DATETIME DEFAULT CURRENT_TIMESTAMP" +
                                                ")");

                jdbcTemplate.execute(
                                "CREATE TABLE IF NOT EXISTS upload_file (" +
                                                "id INT PRIMARY KEY AUTO_INCREMENT," +
                                                "file_name VARCHAR(300) NOT NULL," +
                                                "original_name VARCHAR(300) NOT NULL," +
                                                "file_path VARCHAR(500) NOT NULL," +
                                                "file_size BIGINT," +
                                                "file_type VARCHAR(50)," +
                                                "category VARCHAR(50)," +
                                                "related_type VARCHAR(30)," +
                                                "related_id INT," +
                                                "upload_time DATETIME DEFAULT CURRENT_TIMESTAMP" +
                                                ")");

                try {
                        jdbcTemplate.update(
                                        "INSERT IGNORE INTO users(username,password,real_name,role) VALUES('admin','admin123','系统管理员','admin')");
                        jdbcTemplate.update(
                                        "INSERT IGNORE INTO users(username,password,real_name,role) VALUES('secretary','secretary123','科研秘书','secretary')");
                        jdbcTemplate.update(
                                        "INSERT IGNORE INTO users(username,password,real_name,role) VALUES('leader','leader123','学院领导','leader')");
                        jdbcTemplate.update(
                                        "INSERT IGNORE INTO users(username,password,real_name,role) VALUES('student','student123','学生张三','student')");
                } catch (Exception ignored) {
                }
        }
}