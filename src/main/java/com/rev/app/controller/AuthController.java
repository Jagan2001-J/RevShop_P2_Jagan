package com.rev.app.controller;

import com.rev.app.dto.UserRegistrationDto;
import com.rev.app.entity.User;
import com.rev.app.service.Interface.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AuthController {

    @Autowired
    private IUserService userService;

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            log.warn("Failed login attempt detected.");
            model.addAttribute("error", "Invalid email or password.");
        } else {
            log.info("Displaying login form.");
        }
        return "login";
    }

    /*
     * @PostMapping("/login")
     * public String login(@RequestParam String email, @RequestParam String
     * password,
     * HttpSession session, RedirectAttributes redirectAttributes) {
     * try {
     * User user = userService.loginUser(email, password);
     * session.setAttribute("user", user);
     * 
     * if (user.getRole() == User.Role.SELLER) {
     * return "redirect:/seller/dashboard";
     * }
     * return "redirect:/";
     * 
     * } catch (RuntimeException e) {
     * redirectAttributes.addFlashAttribute("error", "Invalid email or password.");
     * return "redirect:/login";
     * }
     * }
     */

    @GetMapping("/register-buyer")
    public String showBuyerRegistrationForm(Model model) {
        model.addAttribute("userDto", new UserRegistrationDto());
        model.addAttribute("role", "BUYER");
        return "register";
    }

    @GetMapping("/register-seller")
    public String showSellerRegistrationForm(Model model) {
        model.addAttribute("userDto", new UserRegistrationDto());
        model.addAttribute("role", "SELLER");
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userDto") UserRegistrationDto dto,
            RedirectAttributes redirectAttributes) {
        log.info("Attempting to register new user with email: {}", dto.getEmail());
        try {
            User user = new User();
            user.setName(dto.getName());
            user.setEmail(dto.getEmail());
            user.setPhone(dto.getPhone());
            user.setPassword(dto.getPassword());
            user.setRole(dto.getRole());

            userService.registerUser(user);
            log.info("Registration successful for user: {}", dto.getEmail());
            redirectAttributes.addFlashAttribute("msg", "Registration successful! Please login.");
            return "redirect:/login";

        } catch (RuntimeException e) {
            log.error("Registration failed for user {}: {}", dto.getEmail(), e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register-" + dto.getRole().name().toLowerCase();
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        log.info("User requested logout. Invalidating session.");
        session.invalidate();
        redirectAttributes.addFlashAttribute("msg", "You have been logged out successfully.");
        return "redirect:/login";
    }
}
