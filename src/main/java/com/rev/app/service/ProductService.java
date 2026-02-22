package com.rev.app.service;

import com.rev.app.entity.Product;
import com.rev.app.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private IProductRepository;
    public List<Product> getAllProducts(){
        return repo.findAll();
    }
}
