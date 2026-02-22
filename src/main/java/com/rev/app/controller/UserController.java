package com.rev.app.controller;

import com.rev.app.dto.UserDTO;
import com.rev.app.entity.User;
import com.rev.app.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService service;

    @PostMapping("/register")
    public User register(@RequestBody UserDTO dto){
        return service.register(dto);
    }
    @PostMapping("/login")
    public User login (@RequestParam String email, @RequestParam String password){
        return service.login(email,password);
    }
    @GetMapping("/products")
    public List<products> products(){
        return service.getAllProducts();
    }
}
