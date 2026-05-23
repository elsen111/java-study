package com.studentfilter.model.validation;

import com.studentfilter.validation.ValidGroupCodeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidGroupCodeValidator.class)
public @interface ValidGroupCode {
    String message() default "Invalid group code";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
