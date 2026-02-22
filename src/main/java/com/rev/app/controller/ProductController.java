package com.rev.app.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class ProductController {

    @GetMapping("/products")
    public List<products> products(){
        return service.getAllProducts();
    }
}
