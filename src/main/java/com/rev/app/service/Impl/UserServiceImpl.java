package com.rev.app.service.Impl;

import com.rev.app.entity.User;
import com.rev.app.exception.ResourceNotFoundException;
import com.rev.app.exception.UnauthorizedException;
import com.rev.app.repository.IUserRepository;
import com.rev.app.service.Interface.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user) {
        if (repo.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repo.save(user);
    }

    @Override
    public User loginUser(String email, String password) {
        User u = repo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!u.getPassword().equals(password))
            throw new UnauthorizedException("Invalid Password");

        return u;
    }
}
