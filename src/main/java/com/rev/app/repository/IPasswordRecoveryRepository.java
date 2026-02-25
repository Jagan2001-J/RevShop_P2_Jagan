package com.rev.app.repository;

import com.rev.app.entity.PasswordRecovery;
import com.rev.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPasswordRecoveryRepository extends JpaRepository<PasswordRecovery, Long> {
    Optional<PasswordRecovery> findByToken(String token);

    Optional<PasswordRecovery> findByUser(User user);
}
