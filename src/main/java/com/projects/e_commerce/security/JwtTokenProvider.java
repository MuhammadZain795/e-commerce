package com.projects.e_commerce.security;

import com.projects.e_commerce.domain.user.User;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final String secret = "very-secret-key";

    public String generate(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().name())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }
}
