package com.projects.e_commerce.dto;

import com.projects.e_commerce.user.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginRequest {
    private String email;
    private String password;
    private Role role;
}
