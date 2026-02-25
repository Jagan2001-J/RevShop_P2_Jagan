package com.rev.app.service.Interface;

import com.rev.app.entity.Product;
import java.util.List;

public interface IProductService {
    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product addProduct(Product product);

    Product updateProduct(Product product);

    void deleteProduct(Long id);

    List<Product> getProductsByCategory(Long categoryId);

    List<Product> getProductsBySeller(Long sellerId);
}
