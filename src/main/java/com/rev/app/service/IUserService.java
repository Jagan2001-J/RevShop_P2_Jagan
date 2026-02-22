package com.rev.app.service;

import com.rev.app.dto.UserDTO;
import com.rev.app.entity.User;

public interface IUserService {

    User register(UserDTO dto);
    User login(String email,String password);
}
