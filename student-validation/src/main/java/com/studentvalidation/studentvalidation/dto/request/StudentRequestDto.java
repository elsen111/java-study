package com.studentvalidation.studentvalidation.dto.request;

import com.studentvalidation.studentvalidation.model.validation.CreateGroup;
import com.studentvalidation.studentvalidation.model.validation.UpdateGroup;
import com.studentvalidation.studentvalidation.model.validation.ValidGroupCode;
import com.studentvalidation.studentvalidation.model.validation.ValidStudentId;
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

    @NotNull(groups = CreateGroup.class)
    @ValidStudentId(message = "Invalid id format!", groups = {CreateGroup.class, UpdateGroup.class})
    private String studentId;

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
}
