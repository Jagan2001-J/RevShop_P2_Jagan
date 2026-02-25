package com.rev.app.service.Interface;

import com.rev.app.dto.LoginRequestDTO;
import com.rev.app.dto.RegisterRequestDTO;
import com.rev.app.dto.UserResponseDTO;

public interface IAuthService {
    String login(LoginRequestDTO request);

    UserResponseDTO register(RegisterRequestDTO request);
}
