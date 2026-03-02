package com.rev.app.controller;

import com.rev.app.entity.Product;
import com.rev.app.service.Interface.IProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.rev.app.repository.IUserRepository;
import com.rev.app.config.JwtAuthFilter;
import com.rev.app.service.Interface.ICartService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(HomeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IProductService productService;

    @MockBean
    private ICartService cartService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private IUserRepository userRepository;

    @Test
    public void testIndex_ReturnsViewAndModel() throws Exception {
        List<Product> products = new ArrayList<>();
        Product p1 = new Product();
        p1.setId(1L);
        products.add(p1);

        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("featuredProducts"));
    }
}
