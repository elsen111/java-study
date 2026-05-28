package com.minilibrary.studentmanagementapi.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentRequestDto {
    @NotBlank(message = "First name cannot be blank")
    @Size(min = 2, message = "First name should be minimum 2 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 2, message = "Last name should be minimum 2 characters")
    private String lastName;

    @NotNull(message = "Age cannot be null")
    @Min(value = 16, message = "Age should be minimum 16")
    @Max(value = 65, message = "Age should be maximum 65")
    private Integer age;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number cannot be blank")
    @Size(min = 9, message = "Phone number should be minimum 9 characters")
    private String phoneNumber;
}
