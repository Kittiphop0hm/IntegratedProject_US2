package com.example.backend.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SellerFieldsValidator.class)
@Documented
public @interface ValidSellerFields {
    String message() default "Invalid seller fields";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
