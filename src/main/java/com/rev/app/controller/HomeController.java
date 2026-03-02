package com.rev.app.controller;

import com.rev.app.entity.Product;
import com.rev.app.service.Interface.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {

    @Autowired
    private IProductService productService;

    @GetMapping("/")
    public String index(Model model) {
        log.debug("Loading home page with featured products.");
        // Fetch featured products (for now, simply grab the latest 4 products or random
        // ones)
        List<Product> allProducts = productService.getAllProducts();
        List<Product> featuredProducts = allProducts.stream().limit(8).collect(Collectors.toList());

        log.debug("Fetched {} total products, rendering {} as featured.", allProducts.size(), featuredProducts.size());
        model.addAttribute("categories", Product.Category.values());
        model.addAttribute("featuredProducts", featuredProducts);

        return "index";
    }
}
