package com.studentfilter.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentFilterDto {
    private String fullName;
    private Integer fromAge;
    private Integer toAge;
    private Double fromGpa;
    private Double toGpa;
}
