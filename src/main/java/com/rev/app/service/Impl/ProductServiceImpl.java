package com.rev.app.service.Impl;

import com.rev.app.entity.Product;
import com.rev.app.repository.IProductRepository;
import com.rev.app.service.Interface.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductRepository repo;

    @Override
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public Product addProduct(Product product) {
        return repo.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        Product existing = getProductById(product.getId());
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setDiscountedPrice(product.getDiscountedPrice());
        existing.setQuantity(product.getQuantity());
        existing.setInventoryThreshold(product.getInventoryThreshold());
        existing.setImageUrl(product.getImageUrl());
        existing.setCategory(product.getCategory());
        return repo.save(existing);
    }

    @Override
    public void deleteProduct(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        // Map ID to Category enum for type safety and performance
        if (categoryId < 0 || categoryId >= Product.Category.values().length) {
            return List.of();
        }
        Product.Category category = Product.Category.values()[categoryId.intValue()];
        return repo.findByCategory(category);
    }

    @Override
    public List<Product> getProductsBySeller(Long sellerId) {
        return repo.findAll().stream().filter(p -> p.getSeller() != null && p.getSeller().getId().equals(sellerId))
                .toList();
    }

    @Override
    public List<Product> searchProducts(String name) {
        return repo.findByNameContainingIgnoreCase(name);
    }
}
