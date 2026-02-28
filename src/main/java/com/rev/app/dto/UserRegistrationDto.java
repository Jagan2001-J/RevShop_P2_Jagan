package com.rev.app.dto;

import com.rev.app.entity.User;
import lombok.Data;

@Data
public class UserRegistrationDto {
    private String name;
    private String email;
    private String phone;
    private String password;
    private User.Role role;
}
