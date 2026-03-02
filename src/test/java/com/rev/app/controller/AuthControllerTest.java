package com.rev.app.controller;

import com.rev.app.dto.UserRegistrationDto;
import com.rev.app.entity.User;
import com.rev.app.service.Interface.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.rev.app.repository.IUserRepository;
import com.rev.app.config.JwtAuthFilter;
import com.rev.app.service.Interface.ICartService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserService userService;

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
    public void testShowLoginForm() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeDoesNotExist("error"));
    }

    @Test
    public void testShowLoginFormWithError() throws Exception {
        mockMvc.perform(get("/login").param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Invalid email or password."));
    }

    @Test
    public void testShowBuyerRegistrationForm() throws Exception {
        mockMvc.perform(get("/register-buyer"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("userDto"))
                .andExpect(model().attribute("role", "BUYER"));
    }

    @Test
    public void testShowSellerRegistrationForm() throws Exception {
        mockMvc.perform(get("/register-seller"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("userDto"))
                .andExpect(model().attribute("role", "SELLER"));
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
        User user = new User();
        user.setId(1L);
        when(userService.registerUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/register")
                .param("name", "Test User")
                .param("email", "test@example.com")
                .param("password", "password")
                .param("role", "BUYER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("msg"));
    }

    @Test
    public void testRegisterUser_Failure() throws Exception {
        when(userService.registerUser(any(User.class))).thenThrow(new RuntimeException("Email already exists"));

        mockMvc.perform(post("/register")
                .param("name", "Test User")
                .param("email", "test@example.com")
                .param("password", "password")
                .param("role", "BUYER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register-buyer"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    public void testLogout() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("msg"));
    }
}
