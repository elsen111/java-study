package com.studentfilter.dto.response;

import com.studentfilter.model.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponseDto {
    private Long id;
    private String fullName;
    private String email;
    private String groupCode;
    private Integer age;
    private Double gpa;
    private LocalDateTime createdAt;

    public static StudentResponseDto from(Student student) {
        return StudentResponseDto.builder()
                .id(student.getId())
                .fullName(student.getFullName())
                .email(student.getEmail())
                .groupCode(student.getGroupCode())
                .age(Integer.valueOf(student.getAge()))
                .gpa(student.getGpa())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
