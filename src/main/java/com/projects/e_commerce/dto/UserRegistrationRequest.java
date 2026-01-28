package com.projects.e_commerce.dto;

import com.projects.e_commerce.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(name = "UserRegistrationRequest", description = "DTO for registering a new user")
public class UserRegistrationRequest {

    @Schema(description = "User email address", example = "user@example.com", required = true)
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @Schema(description = "User password (min 6 characters)", example = "secret123", required = true)
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Schema(description = "Role of the user", example = "USER", required = true)
    @NotNull(message = "Role is required")
    private Role role;
}
