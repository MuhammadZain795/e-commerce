package com.projects.e_commerce.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    private UserDetails mockUser() {
        return User.builder()
                .username("test@mail.com")
                .password("password")
                .roles("USER")
                .build();
    }

    @Test
    void shouldGenerateToken() {

        UserDetails userDetails = mockUser();

        String token = jwtService.generateToken(userDetails);

        assertThat(token).isNotNull();
        assertThat(token).isNotBlank();
    }

    @Test
    void shouldExtractUsernameFromToken() {

        UserDetails userDetails = mockUser();
        String token = jwtService.generateToken(userDetails);

        String username = jwtService.extractUsername(token);

        assertThat(username).isEqualTo("test@mail.com");
    }

    @Test
    void shouldValidateTokenSuccessfully() {

        UserDetails userDetails = mockUser();
        String token = jwtService.generateToken(userDetails);

        boolean valid = jwtService.isTokenValid(token, userDetails);

        assertThat(valid).isTrue();
    }

    @Test
    void shouldFailValidationForDifferentUser() {

        UserDetails originalUser = mockUser();
        String token = jwtService.generateToken(originalUser);

        UserDetails anotherUser = User.builder()
                .username("other@mail.com")
                .password("password")
                .roles("USER")
                .build();

        boolean valid = jwtService.isTokenValid(token, anotherUser);

        assertThat(valid).isFalse();
    }
}
