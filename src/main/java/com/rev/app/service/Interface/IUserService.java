package com.rev.app.service.Interface;

import com.rev.app.entity.User;

public interface IUserService {
    User registerUser(User user);

    User loginUser(String email, String password);

    User getUserByEmail(String email);

    void updatePassword(String email, String newPassword);
}
