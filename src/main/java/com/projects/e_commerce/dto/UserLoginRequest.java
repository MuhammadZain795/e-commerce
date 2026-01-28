package com.projects.e_commerce.dto;

import com.projects.e_commerce.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(name = "UserLoginRequest", description = "DTO for user login request")
public class UserLoginRequest {

    @Schema(description = "User email address", example = "user@example.com", required = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(description = "User password", example = "secret123", required = true)
    @NotBlank(message = "Password is required")
    private String password;

    @Schema(description = "Role of the user", example = "USER", required = true)
    @NotNull(message = "Role is required")
    private Role role;
}
