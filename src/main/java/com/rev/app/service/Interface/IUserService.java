package com.rev.app.service.Interface;

import com.rev.app.dto.UserResponseDTO;
import com.rev.app.entity.User;

public interface IUserService {

    User register(UserResponseDTO dto);
    User login(String email,String password);
}
