package com.rev.app.controller;

import com.rev.app.entity.Cart;
import com.rev.app.entity.User;
import com.rev.app.service.Interface.ICartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ICartService cartService;

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Cart cart = cartService.getCartByUserId(user.getId());
        model.addAttribute("cart", cart);

        return "buyer/cart";
    }

    @PostMapping("/add")
    public String addToCart(
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") Integer qty,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            cartService.addToCart(user.getId(), productId, qty);
            redirectAttributes.addFlashAttribute("msg", "Item added to cart successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add item to cart: " + e.getMessage());
        }

        return "redirect:/buyer/products/" + productId;
    }

    @PostMapping("/update")
    public String updateCartItem(
            @RequestParam Long cartItemId,
            @RequestParam Integer qty,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            if (qty <= 0) {
                cartService.removeCartItem(cartItemId);
                redirectAttributes.addFlashAttribute("msg", "Item removed from cart.");
            } else {
                cartService.updateCartItemQuantity(cartItemId, qty);
                redirectAttributes.addFlashAttribute("msg", "Cart updated successfully.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update cart: " + e.getMessage());
        }

        return "redirect:/cart";
    }

    @GetMapping("/remove/{itemId}")
    public String removeCartItem(@PathVariable Long itemId, HttpSession session,
            RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            cartService.removeCartItem(itemId);
            redirectAttributes.addFlashAttribute("msg", "Item removed from cart.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to remove item: " + e.getMessage());
        }

        return "redirect:/cart";
    }
}
