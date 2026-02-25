package com.rev.app.service.Impl;

import com.rev.app.entity.User;
import com.rev.app.repository.IUserRepository;
import com.rev.app.service.Interface.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository repo;

    @Override
    public User registerUser(User user) {
        return repo.save(user);
    }

    @Override
    public User loginUser(String email, String password) {
        User u = repo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (!u.getPassword().equals(password))
            throw new RuntimeException("Invalid Password");

        return u;
    }
}
