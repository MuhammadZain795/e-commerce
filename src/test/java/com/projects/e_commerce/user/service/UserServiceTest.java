package com.projects.e_commerce.user.service;

import com.projects.e_commerce.repository.UserRepository;
import com.projects.e_commerce.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUser() {

        User user = User.builder()
                .email("test@example.com")
                .password("plain")
                .build();

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("plain")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User saved = userService.createUser(user);

        assertEquals("encoded", saved.getPassword());
        verify(userRepository).save(saved);
    }

    @Test
    void shouldThrowException_whenEmailExists() {
        User user = User.builder().email("exists@test.com").build();
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.createUser(user));

        assertEquals("Email already exists", ex.getMessage());
    }

    @Test
    void shouldFindUserById() {
        User user = User.builder().id(1L).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void shouldFindUserByEmail() {
        User user = User.builder().email("test@mail.com").build();
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));

        User result = userService.findByEmail("test@mail.com");

        assertEquals("test@mail.com", result.getEmail());
    }

    @Test
    void shouldGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(new User(), new User()));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
    }

    @Test
    void shouldDeleteUser() {
        User user = User.builder().id(1L).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository).delete(user);
    }
}
