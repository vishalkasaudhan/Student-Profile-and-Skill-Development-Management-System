package com.exam.student.dto;

import java.util.List;

/** A group of students who have exactly the same set of technical skills. */
public class SkillGroup {

    private List<String> skills;
    private List<String> students;

    public SkillGroup() {
    }

    public SkillGroup(List<String> skills, List<String> students) {
        this.skills = skills;
        this.students = students;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getStudents() {
        return students;
    }

    public void setStudents(List<String> students) {
        this.students = students;
    }
}
