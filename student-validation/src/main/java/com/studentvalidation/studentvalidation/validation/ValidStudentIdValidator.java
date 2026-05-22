package com.studentvalidation.studentvalidation.validation;


import com.studentvalidation.studentvalidation.model.validation.ValidStudentId;
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
