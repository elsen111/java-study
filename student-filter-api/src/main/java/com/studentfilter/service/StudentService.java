package com.studentfilter.service;

import com.studentfilter.dto.request.StudentRequestDto;
import com.studentfilter.dto.response.StudentResponseDto;
import com.studentfilter.model.Student;
import com.studentfilter.model.StudentFilterDto;
import com.studentfilter.repositories.StudentRepository;
import com.studentfilter.service.specification.StudentSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<StudentResponseDto> getAllStudents(StudentFilterDto studentFilterDto) {
        var specification = Specification.where(new StudentSpecification(studentFilterDto));

        return studentRepository.findAll(specification)
                .stream()
                .map(StudentResponseDto::from)
                .collect(Collectors.toList());
    }

    public StudentResponseDto createStudent(StudentRequestDto request) {
        Student student = Student.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .groupCode(request.getGroupCode())
                .age(String.valueOf(request.getAge()))
                .gpa(request.getGpa())
                .build();

        Student savedStudent = studentRepository.save(student);
        return StudentResponseDto.from(savedStudent);
    }

    public StudentResponseDto updateStudent(Long id, StudentRequestDto request) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tələbə tapılmadı! ID: " + id));

        existingStudent.setFullName(request.getFullName());
        existingStudent.setEmail(request.getEmail());
        existingStudent.setGroupCode(request.getGroupCode());
        existingStudent.setAge(String.valueOf(request.getAge()));
        existingStudent.setGpa(request.getGpa());

        Student savedStudent = studentRepository.save(existingStudent);
        return StudentResponseDto.from(savedStudent);
    }
}
