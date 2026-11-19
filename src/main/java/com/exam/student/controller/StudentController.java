package com.exam.student.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exam.student.dto.MeritListEntry;
import com.exam.student.dto.SkillGroup;
import com.exam.student.dto.StudentUpdateRequest;
import com.exam.student.model.Student;
import com.exam.student.service.StudentService;
import org.springframework.web.bind.annotation.CrossOrigin;
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // Q3.1 Add a student
    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Student saved = studentService.addStudent(student);
        return new ResponseEntity<Student>(saved, HttpStatus.CREATED);
    }

    // Q3.2 Retrieve all students
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return new ResponseEntity<List<Student>>(studentService.getAllStudents(), HttpStatus.OK);
    }

    // Q3.6 Display the merit list
    @GetMapping("/merit-list")
    public ResponseEntity<List<MeritListEntry>> getMeritList() {
        return new ResponseEntity<List<MeritListEntry>>(studentService.getMeritList(), HttpStatus.OK);
    }

    // Q2.4 Search by ID, email or course
    @GetMapping("/search")
    public ResponseEntity<List<Student>> searchStudents(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "course", required = false) String course) {
        return new ResponseEntity<List<Student>>(studentService.searchStudents(id, email, course), HttpStatus.OK);
    }

    // Q2.6 Students having exactly the same set of technical skills
    @GetMapping("/same-skills")
    public ResponseEntity<List<SkillGroup>> getSameSkillGroups() {
        return new ResponseEntity<List<SkillGroup>>(studentService.getStudentsWithSameSkillSet(), HttpStatus.OK);
    }

    // Q2.6 Students sharing each individual skill
    @GetMapping("/same-skills/by-skill")
    public ResponseEntity<Map<String, List<String>>> getStudentsBySharedSkill() {
        return new ResponseEntity<Map<String, List<String>>>(studentService.getStudentsBySharedSkill(),
                HttpStatus.OK);
    }

    // Q2.7 Programming < 50 (needs improvement) but mathematics >= 75
    @GetMapping("/needs-programming-improvement")
    public ResponseEntity<List<Student>> getNeedsProgrammingImprovement() {
        return new ResponseEntity<List<Student>>(
                studentService.getNeedsProgrammingImprovementButStrongInMaths(), HttpStatus.OK);
    }

    // Q3.3 Search for a student by ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable("id") String id) {
        return new ResponseEntity<Student>(studentService.getStudentById(id), HttpStatus.OK);
    }

    // Q3.4 Update student scores and skills
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable("id") String id,
            @RequestBody StudentUpdateRequest request) {
        return new ResponseEntity<Student>(studentService.updateStudent(id, request), HttpStatus.OK);
    }

    // Q3.5 Delete a student record
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteStudent(@PathVariable("id") String id) {
        studentService.deleteStudent(id);
        Map<String, String> body = new HashMap<String, String>();
        body.put("message", "Student deleted successfully with ID: " + id);
        return new ResponseEntity<Map<String, String>>(body, HttpStatus.OK);
    }
}
