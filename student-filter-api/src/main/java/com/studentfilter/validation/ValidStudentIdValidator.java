package com.studentfilter.validation;


import com.studentfilter.model.validation.ValidStudentId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidStudentIdValidator implements ConstraintValidator<ValidStudentId, String> {

    @Override
    public boolean isValid(String studentId, ConstraintValidatorContext constraintValidatorContext) {
        if(studentId == null){
            return true;
        }

        return studentId.matches("^ST\\d{7}$");
    }
}
