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

@Service
public class PasswordRecoveryServiceImpl implements IPasswordRecoveryService {

    @Autowired
    private IPasswordRecoveryRepository passwordRecoveryRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void initiateRecovery(String email) {
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
            System.out.println("Recovery token generated for " + email + ": " + token);
        }
    }

    @Override
    public boolean validateToken(String token) {
        Optional<PasswordRecovery> recoveryOpt = passwordRecoveryRepository.findByToken(token);

        if (recoveryOpt.isPresent()) {
            PasswordRecovery recovery = recoveryOpt.get();
            // Check if it's expired
            return recovery.getExpiresAt().isAfter(LocalDateTime.now());
        }

        return false;
    }

    @Override
    public void updatePassword(String token, String newPassword) {
        PasswordRecovery recovery = passwordRecoveryRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (recovery.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = recovery.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Delete successful token to prevent reuse
        passwordRecoveryRepository.delete(recovery);
    }
}
