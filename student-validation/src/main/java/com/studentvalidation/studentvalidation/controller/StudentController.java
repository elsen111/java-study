package com.studentvalidation.studentvalidation.controller;

import com.studentvalidation.studentvalidation.dto.request.StudentRequestDto;
import com.studentvalidation.studentvalidation.model.validation.CreateGroup;
import com.studentvalidation.studentvalidation.model.validation.UpdateGroup;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
public class StudentController {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String create(
            @Validated(CreateGroup.class)
            @RequestBody StudentRequestDto request
    ) {
        return "Student Created";
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String update(
            @PathVariable Integer id,
            @Validated(UpdateGroup.class)
            @RequestBody StudentRequestDto request
    ) {
        return "Student Created with id: " + id;
    }
}
