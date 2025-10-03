package com.busify.project.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
    private String name;
    private String phoneNumber;
    @NotBlank(message = "Email not blank")
    @Email(message = "Email is not valid")
    private String email;
    @NotBlank(message = "Password not blank")
    @Min(value = 6, message = "Password must be at least 6 characters long")
    private String password;
}
