package com.minilibrary.minilibrary.controller;

import com.minilibrary.minilibrary.dto.response.BorrowResponse;
import com.minilibrary.minilibrary.dto.request.StudentCreateRequest;
import com.minilibrary.minilibrary.dto.response.StudentResponse;
import com.minilibrary.minilibrary.service.BorrowService;
import com.minilibrary.minilibrary.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final BorrowService borrowService;

    public StudentController(StudentService studentService, BorrowService borrowService) {
        this.studentService = studentService;
        this.borrowService = borrowService;
    }

    @PostMapping
    public String create(@RequestBody StudentCreateRequest request) {
        studentService.create(request);
        return "Student created successfully";
    }

    @GetMapping
    public List<StudentResponse> getAll() {
        return studentService.getAll();
    }

    @GetMapping("/{id}")
    public StudentResponse getById(@PathVariable Long id) {
        return studentService.getById(id);
    }

    @GetMapping("/{studentId}/borrows")
    public List<BorrowResponse> getStudentBorrows(@PathVariable Long studentId) {
        return borrowService.getStudentBorrows(studentId);
    }
}