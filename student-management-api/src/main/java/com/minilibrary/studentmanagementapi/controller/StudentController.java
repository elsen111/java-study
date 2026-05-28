package com.minilibrary.studentmanagementapi.controller;

import com.minilibrary.studentmanagementapi.dto.request.StudentRequestDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    @PostMapping
    public String createStudent(@Valid @RequestBody StudentRequestDto request) {
        return request.toString();
    }
}
