package com.exam.student.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exam.student.dto.MeritListEntry;
import com.exam.student.dto.SkillGroup;
import com.exam.student.dto.StudentUpdateRequest;
import com.exam.student.exception.DuplicateStudentException;
import com.exam.student.exception.StudentNotFoundException;
import com.exam.student.exception.StudentValidationException;
import com.exam.student.model.Student;
import com.exam.student.repository.StudentRepository;

@Service
public class StudentService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Autowired
    private StudentRepository studentRepository;

    // =====================================================
    //  Basic CRUD
    // =====================================================

    /** Add a student: validates data and prevents duplicate student IDs. */
    @Transactional
    public Student addStudent(Student student) {
        if (student == null) {
            throw new StudentValidationException("Student data is required");
        }
        validateAndClean(student);
        if (studentRepository.existsById(student.getStudentId())) {
            throw new DuplicateStudentException("Student ID already exists: " + student.getStudentId());
        }
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(String id) {
        return studentRepository.findById(id.trim())
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + id));
    }

    /** Update scores and/or skills of an existing student. */
    @Transactional
    public Student updateStudent(String id, StudentUpdateRequest request) {
        Student student = getStudentById(id);

        if (request == null || (request.getProgrammingScore() == null
                && request.getMathematicsScore() == null && request.getSkills() == null)) {
            throw new StudentValidationException(
                    "Provide at least one of: programmingScore, mathematicsScore, skills");
        }
        if (request.getProgrammingScore() != null) {
            validateScore(request.getProgrammingScore(), "Programming score");
            student.setProgrammingScore(request.getProgrammingScore());
        }
        if (request.getMathematicsScore() != null) {
            validateScore(request.getMathematicsScore(), "Mathematics score");
            student.setMathematicsScore(request.getMathematicsScore());
        }
        if (request.getSkills() != null) {
            List<String> cleaned = cleanSkills(request.getSkills());
            student.getSkills().clear();
            student.getSkills().addAll(cleaned);
        }
        return studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(String id) {
        Student student = getStudentById(id);
        studentRepository.delete(student);
    }

    // =====================================================
    //  Q2.4  Search by ID, email or course
    // =====================================================
    public List<Student> searchStudents(String id, String email, String course) {
        boolean noId = (id == null || id.isBlank());
        boolean noEmail = (email == null || email.isBlank());
        boolean noCourse = (course == null || course.isBlank());
        if (noId && noEmail && noCourse) {
            throw new StudentValidationException("Provide at least one search parameter: id, email or course");
        }
        return studentRepository.findAll().stream()
                .filter(s -> noId || s.getStudentId().equalsIgnoreCase(id.trim()))
                .filter(s -> noEmail || s.getEmail().equalsIgnoreCase(email.trim()))
                .filter(s -> noCourse || s.getCourseName().equalsIgnoreCase(course.trim()))
                .collect(Collectors.toList());
    }

    // =====================================================
    //  Q2.5  Merit list (average score, descending)
    // =====================================================
    public List<MeritListEntry> getMeritList() {
        List<Student> sorted = new ArrayList<Student>(studentRepository.findAll());
        Comparator<Student> byAverage = Comparator.comparingDouble(Student::getAverageScore);
        sorted.sort(byAverage.reversed().thenComparing(Student::getStudentName));

        List<MeritListEntry> merit = new ArrayList<MeritListEntry>();
        int rank = 0;
        double previousAverage = -1;
        for (int i = 0; i < sorted.size(); i++) {
            Student s = sorted.get(i);
            if (s.getAverageScore() != previousAverage) {
                rank = i + 1; // equal averages share the same rank
                previousAverage = s.getAverageScore();
            }
            merit.add(new MeritListEntry(rank, s.getStudentId(), s.getStudentName(), s.getCourseName(),
                    s.getAverageScore(), s.getPerformanceCategory()));
        }
        return merit;
    }

    // =====================================================
    //  Q2.6  Students having the same technical skills
    // =====================================================

    /** Groups students who have EXACTLY the same set of skills (ignoring case and order). */
    public List<SkillGroup> getStudentsWithSameSkillSet() {
        // key = sorted, lower-case skills joined by comma -> list of students
        Map<String, List<Student>> groups = new LinkedHashMap<String, List<Student>>();
        for (Student s : studentRepository.findAll()) {
            TreeSet<String> skillSet = new TreeSet<String>();
            for (String skill : s.getSkills()) {
                skillSet.add(skill.trim().toLowerCase());
            }
            if (skillSet.isEmpty()) {
                continue;
            }
            String key = String.join(",", skillSet);
            groups.computeIfAbsent(key, k -> new ArrayList<Student>()).add(s);
        }

        List<SkillGroup> result = new ArrayList<SkillGroup>();
        for (Map.Entry<String, List<Student>> entry : groups.entrySet()) {
            if (entry.getValue().size() >= 2) {
                List<String> skills = new ArrayList<String>();
                for (String skill : entry.getKey().split(",")) {
                    skills.add(skill);
                }
                List<String> students = entry.getValue().stream()
                        .map(st -> st.getStudentId() + " - " + st.getStudentName())
                        .collect(Collectors.toList());
                result.add(new SkillGroup(skills, students));
            }
        }
        return result;
    }

    /** For every skill, lists the students who have it (only skills shared by 2 or more students). */
    public Map<String, List<String>> getStudentsBySharedSkill() {
        Map<String, List<String>> skillMap = new TreeMap<String, List<String>>();
        for (Student s : studentRepository.findAll()) {
            TreeSet<String> uniqueSkills = new TreeSet<String>();
            for (String skill : s.getSkills()) {
                uniqueSkills.add(skill.trim().toLowerCase());
            }
            for (String skill : uniqueSkills) {
                skillMap.computeIfAbsent(skill, k -> new ArrayList<String>())
                        .add(s.getStudentId() + " - " + s.getStudentName());
            }
        }
        Map<String, List<String>> shared = new TreeMap<String, List<String>>();
        for (Map.Entry<String, List<String>> entry : skillMap.entrySet()) {
            if (entry.getValue().size() >= 2) {
                shared.put(entry.getKey(), entry.getValue());
            }
        }
        return shared;
    }

    // =====================================================
    //  Q2.7  Needs improvement in programming (< 50) but mathematics >= 75
    // =====================================================
    public List<Student> getNeedsProgrammingImprovementButStrongInMaths() {
        return studentRepository.findAll().stream()
                .filter(s -> s.getProgrammingScore() < 50)
                .filter(s -> s.getMathematicsScore() >= 75)
                .collect(Collectors.toList());
    }

    // =====================================================
    //  Q2.8  Validation helpers
    // =====================================================
    private void validateAndClean(Student s) {
        if (isBlank(s.getStudentId())) {
            throw new StudentValidationException("Student ID is required");
        }
        s.setStudentId(s.getStudentId().trim());
        if (s.getStudentId().length() > 20) {
            throw new StudentValidationException("Student ID must be at most 20 characters");
        }
        if (isBlank(s.getStudentName())) {
            throw new StudentValidationException("Student name is required");
        }
        s.setStudentName(s.getStudentName().trim());

        if (isBlank(s.getEmail()) || !EMAIL_PATTERN.matcher(s.getEmail().trim()).matches()) {
            throw new StudentValidationException("A valid email address is required");
        }
        s.setEmail(s.getEmail().trim());

        if (isBlank(s.getCourseName())) {
            throw new StudentValidationException("Course name is required");
        }
        s.setCourseName(s.getCourseName().trim());

        validateScore(s.getProgrammingScore(), "Programming score");
        validateScore(s.getMathematicsScore(), "Mathematics score");

        s.setSkills(cleanSkills(s.getSkills()));
    }

    private void validateScore(Double score, String label) {
        if (score == null) {
            throw new StudentValidationException(label + " is required");
        }
        if (score.isNaN() || score < 0 || score > 100) {
            throw new StudentValidationException(label + " must be between 0 and 100");
        }
    }

    /** Trims skills, removes blanks and duplicates (case-insensitive), keeps original order. */
    private List<String> cleanSkills(List<String> skills) {
        List<String> cleaned = new ArrayList<String>();
        if (skills == null) {
            return cleaned;
        }
        List<String> seen = new ArrayList<String>();
        for (String skill : skills) {
            if (skill == null || skill.isBlank()) {
                continue;
            }
            String trimmed = skill.trim();
            if (!seen.contains(trimmed.toLowerCase())) {
                seen.add(trimmed.toLowerCase());
                cleaned.add(trimmed);
            }
        }
        return cleaned;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
