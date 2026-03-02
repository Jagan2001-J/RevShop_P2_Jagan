package com.rev.app.service.Impl;

import com.rev.app.entity.PasswordRecovery;
import com.rev.app.entity.User;
import com.rev.app.repository.IPasswordRecoveryRepository;
import com.rev.app.repository.IUserRepository;
import com.rev.app.service.Interface.IPasswordRecoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PasswordRecoveryServiceImpl implements IPasswordRecoveryService {

    @Autowired
    private IPasswordRecoveryRepository passwordRecoveryRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void initiateRecovery(String email) {
        log.info("Initiating password recovery for email: {}", email);
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Clear old tokens for this user avoiding duplicates
            passwordRecoveryRepository.findByUser(user).ifPresent(token -> {
                passwordRecoveryRepository.delete(token);
            });

            // Generate a secure UUID token
            String token = UUID.randomUUID().toString();

            PasswordRecovery recovery = PasswordRecovery.builder()
                    .user(user)
                    .token(token)
                    .expiresAt(LocalDateTime.now().plusHours(1)) // 1 hour expiration
                    .build();

            passwordRecoveryRepository.save(recovery);

            // In a real application, you'd send an email here calling an email service:
            // emailService.sendRecoveryEmail(user.getEmail(), token);
            log.info("Recovery token generated for {}: {}", email, token);
        } else {
            log.warn("Password recovery failed: Email {} not found.", email);
        }
    }

    @Override
    public boolean validateToken(String token) {
        log.debug("Validating recovery token.");
        Optional<PasswordRecovery> recoveryOpt = passwordRecoveryRepository.findByToken(token);

        if (recoveryOpt.isPresent()) {
            PasswordRecovery recovery = recoveryOpt.get();
            // Check if it's expired
            boolean isValid = recovery.getExpiresAt().isAfter(LocalDateTime.now());
            if (!isValid)
                log.warn("Validated token is expired.");
            return isValid;
        }

        return false;
    }

    @Override
    public void updatePassword(String token, String newPassword) {
        log.info("Attempting to update password with recovery token.");
        PasswordRecovery recovery = passwordRecoveryRepository.findByToken(token)
                .orElseThrow(() -> {
                    log.error("Failed to update password. Invalid token provided.");
                    return new RuntimeException("Invalid token");
                });

        if (recovery.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("Failed to update password: Token expired.");
            throw new RuntimeException("Token expired");
        }

        User user = recovery.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Delete successful token to prevent reuse
        passwordRecoveryRepository.delete(recovery);
        log.info("Successfully updated password for user ID: {}", user.getId());
    }
}
