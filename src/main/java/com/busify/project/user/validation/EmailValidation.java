// package com.busify.project.user.validation;

// import jakarta.validation.Constraint;
// import jakarta.validation.ConstraintValidator;
// import jakarta.validation.ConstraintValidatorContext;
// import jakarta.validation.Payload;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;
// import com.busify.project.user.repository.UserRepository;

// import java.lang.annotation.*;

// /**
//  * Class chứa tất cả validation liên quan đến Email
//  */
// public class EmailValidation {
    
//     /**
//      * Custom validation annotation to check if email is unique
//      */
//     @Documented
//     @Constraint(validatedBy = UniqueEmailValidator.class)
//     @Target({ElementType.FIELD})
//     @Retention(RetentionPolicy.RUNTIME)
//     public @interface UniqueEmail {
//         String message() default "Email đã tồn tại";
//         Class<?>[] groups() default {};
//         Class<? extends Payload>[] payload() default {};
//     }

//     /**
//      * Validator class for UniqueEmail annotation
//      */
//     @Component
//     public static class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

//         @Autowired
//         private UserRepository userRepository;

//         @Override
//         public boolean isValid(String email, ConstraintValidatorContext context) {
//             if (email == null || email.isEmpty()) {
//                 return true; // Để @NotBlank xử lý
//             }
//             return !userRepository.existsByEmail(email);
//         }
//     }
// }