package com.projects.e_commerce.dto;

import com.projects.e_commerce.user.Role;
import lombok.Data;

@Data
public class UserLoginRequest {
    private String email;
    private String password;
    private Role role;
}
