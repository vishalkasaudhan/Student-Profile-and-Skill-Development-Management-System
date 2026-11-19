package com.exam.student.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

/**
 * Q1: Student class (JPA entity) using encapsulation: private fields,
 * constructors, getters/setters and object methods (business methods,
 * toString, equals, hashCode).
 */
@Entity
@Table(name = "students")
public class Student {

    @Id
    @Column(name = "student_id", length = 20)
    private String studentId;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "programming_score", nullable = false)
    private Double programmingScore;

    @Column(name = "mathematics_score", nullable = false)
    private Double mathematicsScore;

    // List of technical skills -> stored in a separate table "student_skills"
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "student_skills", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<String>();

    public Student() {
    }

    public Student(String studentId, String studentName, String email, String courseName,
            Double programmingScore, Double mathematicsScore, List<String> skills) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.email = email;
        this.courseName = courseName;
        this.programmingScore = programmingScore;
        this.mathematicsScore = mathematicsScore;
        setSkills(skills);
    }

    // ------------- Business (object) methods -------------

    /** Raw average of the two scores (not rounded). */
    private double rawAverage() {
        double p = (programmingScore == null) ? 0.0 : programmingScore.doubleValue();
        double m = (mathematicsScore == null) ? 0.0 : mathematicsScore.doubleValue();
        return (p + m) / 2.0;
    }

    /** Q2.1 - Average score rounded to 2 decimals. */
    public double getAverageScore() {
        return Math.round(rawAverage() * 100.0) / 100.0;
    }

    /** Q2.2 - Performance category based on the average. */
    public String getPerformanceCategory() {
        double avg = rawAverage();
        if (avg >= 85) {
            return "Excellent";
        } else if (avg >= 70) {
            return "Good";
        } else if (avg >= 50) {
            return "Satisfactory";
        } else {
            return "Needs Improvement";
        }
    }

    /** Q2.3 - Stronger subject of the student. */
    public String getStrongerSubject() {
        double p = (programmingScore == null) ? 0.0 : programmingScore.doubleValue();
        double m = (mathematicsScore == null) ? 0.0 : mathematicsScore.doubleValue();
        if (p > m) {
            return "Programming";
        } else if (m > p) {
            return "Mathematics";
        }
        return "Both Equal";
    }

    // ------------- Getters and Setters -------------

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Double getProgrammingScore() {
        return programmingScore;
    }

    public void setProgrammingScore(Double programmingScore) {
        this.programmingScore = programmingScore;
    }

    public Double getMathematicsScore() {
        return mathematicsScore;
    }

    public void setMathematicsScore(Double mathematicsScore) {
        this.mathematicsScore = mathematicsScore;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = (skills == null) ? new ArrayList<String>() : new ArrayList<String>(skills);
    }

    // ------------- Object methods -------------

    @Override
    public String toString() {
        return "Student [studentId=" + studentId + ", studentName=" + studentName + ", email=" + email
                + ", courseName=" + courseName + ", programmingScore=" + programmingScore
                + ", mathematicsScore=" + mathematicsScore + ", skills=" + skills + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student other = (Student) o;
        return Objects.equals(studentId, other.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }
}
