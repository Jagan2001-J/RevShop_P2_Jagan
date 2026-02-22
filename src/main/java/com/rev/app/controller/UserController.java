package com.rev.app.controller;

import com.rev.app.dto.UserResponseDTO;
import com.rev.app.entity.User;
import com.rev.app.service.Interface.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService service;

    @PostMapping("/register")
    public User register(@RequestBody UserResponseDTO dto){
        return service.register(dto);
    }
    @PostMapping("/login")
    public User login (@RequestParam String email, @RequestParam String password){
        return service.login(email,password);
    }
}
