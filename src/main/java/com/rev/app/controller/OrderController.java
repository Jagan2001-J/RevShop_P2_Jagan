package com.rev.app.controller;

import com.rev.app.entity.Order;
import com.rev.app.entity.User;
import com.rev.app.service.Interface.IOrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @PostMapping("/place")
    public String placeOrder(
            @RequestParam Long shippingAddressId,
            @RequestParam Long billingAddressId,
            @RequestParam String paymentMethod,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            Order order = orderService.placeOrder(user.getId(), shippingAddressId, billingAddressId, paymentMethod);
            redirectAttributes.addFlashAttribute("msg", "Order placed successfully! Order ID: " + order.getId());
            return "redirect:/order/history";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to place order: " + e.getMessage());
            return "redirect:/checkout";
        }
    }

    @GetMapping("/history")
    public String viewOrderHistory(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<Order> orders = orderService.getOrdersByUser(user.getId());
        model.addAttribute("orders", orders);

        return "buyer/orders";
    }

    @GetMapping("/{id}")
    public String viewOrderDetails(@PathVariable Long id, HttpSession session, Model model,
            RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            Order order = orderService.getOrderById(id);
            if (!order.getUser().getId().equals(user.getId())) {
                throw new Exception("Unauthorized access to order.");
            }
            model.addAttribute("order", order);
            return "buyer/order-details";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Order not found. " + e.getMessage());
            return "redirect:/order/history";
        }
    }
}
