package com.rev.app.mapper;

import com.rev.app.dto.RegisterRequestDTO;
import com.rev.app.dto.UserResponseDTO;
import com.rev.app.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        User.Role role = User.Role.BUYER; // Default role
        try {
            if (dto.getRole() != null) {
                role = User.Role.valueOf(dto.getRole().toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            // Log or handle invalid role
        }

        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .phone(dto.getPhone())
                .role(role)
                .build();
    }

    public UserResponseDTO toDto(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDTO response = new UserResponseDTO();
        response.setName(user.getName());
        response.setEmail(user.getEmail());

        // Exclude returning the hashed password to the client typically,
        // but populating it here as requested by previous dto layout
        // (usually a bad practice for response DTOs).
        response.setPassword(user.getPassword());

        return response;
    }
}
