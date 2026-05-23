package com.studentfilter.controller;

import com.studentfilter.dto.request.StudentRequestDto;
import com.studentfilter.dto.response.StudentResponseDto;
import com.studentfilter.model.StudentFilterDto;
import com.studentfilter.model.validation.CreateGroup;
import com.studentfilter.model.validation.UpdateGroup;
import com.studentfilter.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<StudentResponseDto> findAll(StudentFilterDto studentFilterDto) {
        return studentService.getAllStudents(studentFilterDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String create(
            @Validated(CreateGroup.class)
            @RequestBody StudentRequestDto request
    ) {
        studentService.createStudent(request);
        return "Student created successfully";
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String update(
            @PathVariable Long id,
            @Validated(UpdateGroup.class)
            @RequestBody StudentRequestDto request
    ) {
        studentService.updateStudent(id, request);
        return "Student updated successfully with id: " + id;
    }
}
