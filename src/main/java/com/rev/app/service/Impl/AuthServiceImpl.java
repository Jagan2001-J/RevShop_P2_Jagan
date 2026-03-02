package com.rev.app.service.Impl;

import com.rev.app.config.JwtService;
import com.rev.app.dto.LoginRequestDTO;
import com.rev.app.dto.RegisterRequestDTO;
import com.rev.app.dto.UserResponseDTO;
import com.rev.app.entity.User;
import com.rev.app.mapper.UserMapper;
import com.rev.app.repository.IUserRepository;
import com.rev.app.service.Interface.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public String login(LoginRequestDTO request) {
        log.debug("Authenticating login request for {}", request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed: User {} not found.", request.getEmail());
                    return new RuntimeException("User not found");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Login failed: Invalid credentials for {}", request.getEmail());
            throw new RuntimeException("Invalid credentials");
        }

        log.info("Generating JWT for authenticated user {}", request.getEmail());
        return jwtService.generateToken(user.getEmail());
    }

    @Override
    public UserResponseDTO register(RegisterRequestDTO request) {
        log.info("Processing registration request for {}", request.getEmail());
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("Registration failed: Email {} already exists.", request.getEmail());
            throw new RuntimeException("Email already exists");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        log.debug("Saving new User entity to the repository: {}", request.getEmail());
        User savedUser = userRepository.save(user);

        log.info("Successfully registered user {} with ID {}", savedUser.getEmail(), savedUser.getId());
        return userMapper.toDto(savedUser);
    }
}
