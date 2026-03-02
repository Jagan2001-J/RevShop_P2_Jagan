package com.rev.app.service.Impl;

import com.rev.app.entity.Product;
import com.rev.app.entity.User;
import com.rev.app.repository.IProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private IProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private User seller;

    @BeforeEach
    public void setUp() {
        seller = User.builder().id(1L).name("Seller").email("seller@example.com").build();
        product = Product.builder()
                .id(1L)
                .name("Laptop")
                .description("Gaming laptop")
                .price(new BigDecimal("999.99"))
                .quantity(10)
                .category(Product.Category.ELECTRONICS)
                .seller(seller)
                .build();
    }

    @Test
    public void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(product));
        List<Product> products = productService.getAllProducts();
        assertThat(products).hasSize(1);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testGetProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Product found = productService.getProductById(1L);
        assertThat(found.getName()).isEqualTo("Laptop");
    }

    @Test
    public void testAddProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);
        Product saved = productService.addProduct(product);
        assertThat(saved).isNotNull();
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testUpdateProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updatedInfo = Product.builder()
                .id(1L)
                .name("Updated Laptop")
                .price(new BigDecimal("1099.99"))
                .build();

        Product result = productService.updateProduct(updatedInfo);

        assertThat(result.getName()).isEqualTo("Updated Laptop");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testDeleteProduct() {
        doNothing().when(productRepository).deleteById(1L);
        productService.deleteProduct(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetProductsByCategory() {
        when(productRepository.findByCategory(Product.Category.ELECTRONICS)).thenReturn(List.of(product));
        List<Product> products = productService.getProductsByCategory((long) Product.Category.ELECTRONICS.ordinal());
        assertThat(products).isNotEmpty();
    }

    @Test
    public void testSearchProducts() {
        when(productRepository.findByNameContainingIgnoreCase("lap")).thenReturn(List.of(product));
        List<Product> products = productService.searchProducts("lap");
        assertThat(products).hasSize(1);
    }
}
