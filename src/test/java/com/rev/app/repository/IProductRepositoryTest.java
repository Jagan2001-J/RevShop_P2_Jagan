package com.rev.app.repository;

import com.rev.app.entity.Product;
import com.rev.app.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class IProductRepositoryTest {

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IUserRepository userRepository;

    private User seller;

    @BeforeEach
    public void setUp() {
        seller = User.builder()
                .name("Seller")
                .email("seller@example.com")
                .password("password")
                .role(User.Role.SELLER)
                .build();
        userRepository.save(seller);
    }

    @Test
    public void testSaveProduct() {
        Product product = Product.builder()
                .name("Laptop")
                .description("Gaming laptop")
                .price(new BigDecimal("999.99"))
                .quantity(10)
                .category(Product.Category.ELECTRONICS)
                .seller(seller)
                .build();

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindByNameContainingIgnoreCase() {
        Product p1 = Product.builder().name("Apple iPhone").price(new BigDecimal("999")).quantity(5)
                .category(Product.Category.ELECTRONICS).seller(seller).build();
        Product p2 = Product.builder().name("Pineapple").price(new BigDecimal("5")).quantity(10)
                .category(Product.Category.HOME_KITCHEN).seller(seller).build();
        productRepository.save(p1);
        productRepository.save(p2);

        List<Product> products = productRepository.findByNameContainingIgnoreCase("APPLE");

        assertThat(products).hasSize(2);
    }

    @Test
    public void testFindByCategory() {
        Product p1 = Product.builder().name("Jeans").price(new BigDecimal("40")).quantity(20)
                .category(Product.Category.FASHION).seller(seller).build();
        productRepository.save(p1);

        List<Product> products = productRepository.findByCategory(Product.Category.FASHION);

        assertThat(products).isNotEmpty();
        assertThat(products.get(0).getCategory()).isEqualTo(Product.Category.FASHION);
    }
}
