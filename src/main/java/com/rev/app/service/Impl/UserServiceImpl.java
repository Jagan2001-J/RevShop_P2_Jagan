package com.rev.app.service.Impl;

import com.rev.app.dto.UserResponseDTO;
import com.rev.app.entity.User;
import com.rev.app.service.Interface.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserService repo;

    @Override
    public User register(UserResponseDTO dto){
        User u=new User();
        u.setName(dto.getName());
        u.setEmail(dto.getEmail());
        u.setPasswordHash(dto.getPassword());
        u.setRole("BUYER");

        return repo.save(u);
    }
    @Override
    public User login(String email,String password){
        User u=repo.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));

        if(!u.getPasswordHash().equals(password)) throw new RuntimeException("Invalid Password");

        return u;
    }
}
