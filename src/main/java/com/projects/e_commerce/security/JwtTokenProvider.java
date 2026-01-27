package com.projects.e_commerce.security;

import com.projects.e_commerce.user.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    public String generate(User user) {
        String secret = "very-secret-key";
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().name())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }
}
