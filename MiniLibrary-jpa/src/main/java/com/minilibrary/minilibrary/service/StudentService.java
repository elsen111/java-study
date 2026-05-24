package com.minilibrary.minilibrary.service;

import com.minilibrary.minilibrary.dto.request.StudentCreateRequest;
import com.minilibrary.minilibrary.dto.response.StudentResponse;
import com.minilibrary.minilibrary.entity.Student;
import com.minilibrary.minilibrary.exception.BadRequestException;
import com.minilibrary.minilibrary.exception.NotFoundException;
import com.minilibrary.minilibrary.repository.StudentRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public void create(StudentCreateRequest request) {
        if (request.getFullName() == null || request.getFullName().isBlank()) {
            throw new BadRequestException("Full name cannot be empty");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new BadRequestException("Email cannot be empty");
        }

        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        Student student = new Student();
        student.setFullName(request.getFullName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());

        studentRepository.save(student);
    }

    public List<StudentResponse> getAll() {
        return studentRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(StudentResponse::from)
                .toList();
    }

    public StudentResponse getById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found"));
        return StudentResponse.from(student);
    }
}
