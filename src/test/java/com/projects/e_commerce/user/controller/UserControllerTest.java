package com.projects.e_commerce.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.e_commerce.dto.UserLoginRequest;
import com.projects.e_commerce.dto.UserRegistrationRequest;
import com.projects.e_commerce.security.JwtService;
import com.projects.e_commerce.user.Role;
import com.projects.e_commerce.user.entity.User;
import com.projects.e_commerce.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // disables security filters
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private com.projects.e_commerce.security.CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetAllUsers() throws Exception {

        when(userService.getAllUsers()).thenReturn(List.of(new User(), new User()));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteUser() throws Exception {

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }

    @Test
    void shouldRegisterUser() throws Exception {

        UserRegistrationRequest req = UserRegistrationRequest.builder()
                .email("new@mail.com")
                .password("password")
                .role(Role.USER)
                .build();

        User saved = User.builder()
                .id(1L)
                .email(req.getEmail())
                .role(req.getRole())
                .build();

        when(userService.createUser(any())).thenReturn(saved);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("new@mail.com"));
    }

    @Test
    @WithMockUser(username = "test@mail.com")
    void shouldGetUserById() throws Exception {

        User user = User.builder()
                .id(1L)
                .email("test@mail.com")
                .role(Role.USER)
                .build();

        when(userService.findById(1L)).thenReturn(user);
        when(userService.findByEmail("test@mail.com")).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@mail.com"));
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {

        UserLoginRequest req = UserLoginRequest.builder()
                .email("login@mail.com")
                .password("pass")
                .role(Role.USER)
                .build();

        User user = User.builder()
                .email("login@mail.com")
                .password("encoded")
                .role(Role.USER)
                .build();

        when(userService.findByEmail(req.getEmail())).thenReturn(user);
        when(passwordEncoder.matches(req.getPassword(), user.getPassword()))
                .thenReturn(true);
        when(jwtService.generateToken(any())).thenReturn("jwt-token");

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }
}
