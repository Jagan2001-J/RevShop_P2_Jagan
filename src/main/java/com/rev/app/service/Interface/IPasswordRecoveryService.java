package com.rev.app.service.Interface;

public interface IPasswordRecoveryService {
    void initiateRecovery(String email);

    boolean validateToken(String token);

    void updatePassword(String token, String newPassword);
}
