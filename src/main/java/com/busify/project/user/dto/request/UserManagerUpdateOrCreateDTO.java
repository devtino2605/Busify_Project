package com.busify.project.user.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserManagerUpdateOrCreateDTO {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private Long roleId;
    private boolean emailVerified;
}
