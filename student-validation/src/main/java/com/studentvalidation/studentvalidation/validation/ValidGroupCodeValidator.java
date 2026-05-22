package com.studentvalidation.studentvalidation.validation;

import com.studentvalidation.studentvalidation.model.validation.ValidGroupCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidGroupCodeValidator implements ConstraintValidator<ValidGroupCode, String> {

    @Override
    public boolean isValid(String groupCode, ConstraintValidatorContext context) {
        if(groupCode == null) {
            return true;
        }

        return groupCode.matches("^[A-Z]{2}-\\d{3}$");
    }

}
