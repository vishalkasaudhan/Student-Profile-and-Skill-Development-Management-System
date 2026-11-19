-- OPTIONAL: the application creates this database and its tables automatically.
-- Run this file in phpMyAdmin (SQL tab) only if you want sample data to test with.

CREATE DATABASE IF NOT EXISTS student_db;
USE student_db;

CREATE TABLE IF NOT EXISTS students (
    student_id        VARCHAR(20)  NOT NULL PRIMARY KEY,
    student_name      VARCHAR(255) NOT NULL,
    email             VARCHAR(255) NOT NULL,
    course_name       VARCHAR(255) NOT NULL,
    programming_score DOUBLE       NOT NULL CHECK (programming_score BETWEEN 0 AND 100),
    mathematics_score DOUBLE       NOT NULL CHECK (mathematics_score BETWEEN 0 AND 100)
);

CREATE TABLE IF NOT EXISTS student_skills (
    student_id VARCHAR(20)  NOT NULL,
    skill      VARCHAR(255),
    CONSTRAINT fk_skill_student FOREIGN KEY (student_id)
        REFERENCES students(student_id) ON DELETE CASCADE
);

-- Sample data (safe to re-run)
INSERT IGNORE INTO students VALUES
 ('S101','Aarav Sharma','aarav@example.com','B.Tech CSE',92,88),
 ('S102','Diya Patel','diya@example.com','B.Tech CSE',45,82),
 ('S103','Rohan Verma','rohan@example.com','B.Tech IT',68,55),
 ('S104','Sneha Iyer','sneha@example.com','B.Tech IT',78,74),
 ('S105','Kabir Singh','kabir@example.com','BCA',40,35),
 ('S106','Meera Nair','meera@example.com','BCA',55,80);

DELETE FROM student_skills WHERE student_id IN ('S101','S102','S103','S104','S105','S106');
INSERT INTO student_skills (student_id, skill) VALUES
 ('S101','Java'),('S101','Spring Boot'),('S101','MySQL'),
 ('S102','Python'),('S102','SQL'),
 ('S103','Java'),('S103','Spring Boot'),('S103','MySQL'),
 ('S104','Python'),('S104','SQL'),
 ('S105','HTML'),('S105','CSS'),
 ('S106','Java'),('S106','HTML');
