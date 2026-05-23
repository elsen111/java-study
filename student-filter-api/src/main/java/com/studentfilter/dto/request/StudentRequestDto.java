package com.studentfilter.dto.request;

import com.studentfilter.model.validation.CreateGroup;
import com.studentfilter.model.validation.UpdateGroup;
import com.studentfilter.model.validation.ValidGroupCode;
import com.studentfilter.model.validation.ValidStudentId;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequestDto {

    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    private Long id;

    @NotBlank(groups = CreateGroup.class)
    @Email(groups = {CreateGroup.class, UpdateGroup.class})
    private String email;

    @NotNull(groups = CreateGroup.class)
    @ValidGroupCode(message = "Invalid group code!", groups =  {CreateGroup.class, UpdateGroup.class})
    private String groupCode;

    @NotBlank(groups = CreateGroup.class)
    private String fullName;

    @NotNull(groups = CreateGroup.class)
    @Min(value = 16, groups = { CreateGroup.class, UpdateGroup.class})
    private Integer age;

    @NotNull(groups = CreateGroup.class)
    private Double gpa;
}
