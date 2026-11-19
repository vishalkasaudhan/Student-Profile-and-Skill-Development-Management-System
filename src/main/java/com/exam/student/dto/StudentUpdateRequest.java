package com.exam.student.dto;

import java.util.List;

/** Request body for "Update student scores and skills". Any field may be omitted. */
public class StudentUpdateRequest {

    private Double programmingScore;
    private Double mathematicsScore;
    private List<String> skills;

    public StudentUpdateRequest() {
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
        this.skills = skills;
    }
}
