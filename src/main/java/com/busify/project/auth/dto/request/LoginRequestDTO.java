package com.busify.project.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequestDTO {

    @Email
    private String username;

    @NotNull
    private String password;
}
