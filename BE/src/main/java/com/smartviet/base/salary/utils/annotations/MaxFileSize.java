package com.smartviet.base.salary.utils.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MaxFileSizeValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxFileSize {

    String message() default "File size exceeds the allowed limit";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    long value();
}
