package com.projects.e_commerce.user.service;

import com.projects.e_commerce.exception.BadRequestException;
import com.projects.e_commerce.exception.ResourceNotFoundException;
import com.projects.e_commerce.user.entity.User;
import com.projects.e_commerce.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        if (userRepo.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public User findById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public void deleteUser(Long id) {
        User user = findById(id);
        userRepo.delete(user);
    }
}
