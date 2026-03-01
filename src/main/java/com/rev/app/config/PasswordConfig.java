package com.rev.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

            @Override
            public String encode(CharSequence rawPassword) {
                return bcrypt.encode(rawPassword);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                if (encodedPassword == null || encodedPassword.isEmpty()) {
                    return false;
                }
                // Check if it looks like a BCrypt hash
                if (encodedPassword.startsWith("$2a$") || encodedPassword.startsWith("$2b$")) {
                    return bcrypt.matches(rawPassword, encodedPassword);
                }
                // Plain text comparison for legacy users
                return encodedPassword.equals(rawPassword.toString());
            }
        };
    }
}
