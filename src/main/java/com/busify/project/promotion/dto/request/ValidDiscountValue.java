package com.busify.project.promotion.dto.request;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidDiscountValueValidator.class)
@Target({ TYPE })
@Retention(RUNTIME)
public @interface ValidDiscountValue {
    String message() default "Invalid discount value for the selected discount type.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
