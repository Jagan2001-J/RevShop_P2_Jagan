package com.rev.app.controller;

import com.rev.app.entity.Cart;
import com.rev.app.entity.User;
import com.rev.app.service.Interface.ICartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private ICartService cartService;

    @ModelAttribute("cartCount")
    public int getCartCount(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null && user.getRole() == User.Role.BUYER) {
            Cart cart = cartService.getCartByUserId(user.getId());
            if (cart != null && cart.getItems() != null) {
                return cart.getItems().size();
            }
        }
        return 0;
    }
}
