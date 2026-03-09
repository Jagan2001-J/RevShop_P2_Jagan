package com.rev.app.service.Impl;

import com.rev.app.entity.User;
import com.rev.app.exception.ResourceNotFoundException;
import com.rev.app.exception.UnauthorizedException;
import com.rev.app.repository.IUserRepository;
import com.rev.app.service.Interface.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user) {
        log.debug("Attempting to register user with email {}", user.getEmail());
        if (repo.findByEmail(user.getEmail()).isPresent()) {
            log.warn("Registration failed. Email {} already exists.", user.getEmail());
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = repo.save(user);
        log.info("Successfully registered user with ID {}", savedUser.getId());
        return savedUser;
    }

    @Override
    public User loginUser(String email, String password) {
        log.debug("User login attempt for email {}", email);
        User u = repo.findByEmail(email).orElseThrow(() -> {
            log.warn("Login failed. No account found for email {}", email);
            return new ResourceNotFoundException("User not found");
        });

        if (!u.getPassword().equals(password)) {
            log.warn("Login failed. Invalid password provided for email {}", email);
            throw new UnauthorizedException("Invalid Password");
        }

        log.info("User {} successfully logged in.", email);
        return u;
    }

    @Override
    public User getUserByEmail(String email) {
        return repo.findByEmail(email).orElse(null);
    }

    @Override
    public void updatePassword(String email, String newPassword) {
        User user = repo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        repo.save(user);
        log.info("Password updated successfully for user {}", email);
    }
}
