package com.exam.student.dto;

/** One row of the merit list. */
public class MeritListEntry {

    private int rank;
    private String studentId;
    private String studentName;
    private String courseName;
    private double averageScore;
    private String performanceCategory;

    public MeritListEntry() {
    }

    public MeritListEntry(int rank, String studentId, String studentName, String courseName,
            double averageScore, String performanceCategory) {
        this.rank = rank;
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseName = courseName;
        this.averageScore = averageScore;
        this.performanceCategory = performanceCategory;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

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

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public String getPerformanceCategory() {
        return performanceCategory;
    }

    public void setPerformanceCategory(String performanceCategory) {
        this.performanceCategory = performanceCategory;
    }
}
