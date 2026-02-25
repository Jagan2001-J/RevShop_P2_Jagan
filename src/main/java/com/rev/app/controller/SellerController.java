package com.rev.app.controller;

import com.rev.app.entity.*;
import com.rev.app.repository.IOrderItemRepository;
import com.rev.app.service.Interface.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    private IProductService productService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private INotificationService notificationService;
    @Autowired
    private IOrderItemRepository orderItemRepo; // Using repo directly for simplicity to get items by product

    // --- Dashboard Overview ---
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        if (!isSeller(session))
            return "redirect:/login";
        User seller = (User) session.getAttribute("user");

        List<Product> products = productService.getProductsBySeller(seller.getId());
        model.addAttribute("totalProducts", products.size());

        long lowStockCount = products.stream().filter(p -> p.getQuantity() <= 5).count();
        model.addAttribute("lowStockCount", lowStockCount);

        // Normally we would have complex queries for total orders/revenue for a seller.
        // For simplicity, we find OrderItems where the product belongs to the seller.
        List<OrderItem> allSellerOrderItems = new ArrayList<>();
        for (Product product : products) {
            allSellerOrderItems.addAll(orderItemRepo.findByProduct(product));
        }

        long totalOrders = allSellerOrderItems.stream().map(oi -> oi.getOrder().getId()).distinct().count();
        double totalRevenue = allSellerOrderItems.stream()
                .filter(oi -> oi.getOrder().getStatus() != Order.OrderStatus.CANCELLED)
                .mapToDouble(oi -> oi.getPrice().doubleValue() * oi.getQuantity())
                .sum();

        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalRevenue", String.format("%.2f", totalRevenue));

        // Recent generic orders
        List<OrderItem> recentOrderItems = allSellerOrderItems.stream()
                .sorted((oi1, oi2) -> oi2.getOrder().getCreatedAt().compareTo(oi1.getOrder().getCreatedAt()))
                .limit(5)
                .collect(Collectors.toList());
        model.addAttribute("recentOrderItems", recentOrderItems);

        // Notifications
        List<Notification> notifications = notificationService.getNotificationsForUser(seller.getId());
        model.addAttribute("notifications", notifications);

        return "seller/dashboard";
    }

    // --- Inventory Management ---
    @GetMapping("/inventory")
    public String showInventory(HttpSession session, Model model) {
        if (!isSeller(session))
            return "redirect:/login";
        User seller = (User) session.getAttribute("user");

        List<Product> products = productService.getProductsBySeller(seller.getId());
        model.addAttribute("products", products);

        return "seller/inventory";
    }

    @GetMapping("/product/new")
    public String showAddProductForm(HttpSession session, Model model) {
        if (!isSeller(session))
            return "redirect:/login";

        model.addAttribute("product", new Product());
        model.addAttribute("categories", Product.Category.values());
        return "seller/product-form";
    }

    @PostMapping("/product/add")
    public String addProduct(@ModelAttribute Product product, @RequestParam String categoryName,
            HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isSeller(session))
            return "redirect:/login";
        User seller = (User) session.getAttribute("user");

        try {
            product.setCategory(Product.Category.valueOf(categoryName));
            product.setSeller(seller);
            // created and updated normally handled by @PrePersist
            product.setCreatedAt(LocalDateTime.now());
            product.setUpdatedAt(LocalDateTime.now());

            productService.addProduct(product);
            redirectAttributes.addFlashAttribute("msg", "Product added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add product: " + e.getMessage());
        }
        return "redirect:/seller/inventory";
    }

    @GetMapping("/product/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, HttpSession session, Model model,
            RedirectAttributes redirectAttributes) {
        if (!isSeller(session))
            return "redirect:/login";
        User seller = (User) session.getAttribute("user");

        Product product = productService.getProductById(id);
        if (!product.getSeller().getId().equals(seller.getId())) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized to edit this product.");
            return "redirect:/seller/inventory";
        }

        model.addAttribute("product", product);
        model.addAttribute("categories", Product.Category.values());
        return "seller/product-form";
    }

    @PostMapping("/product/update")
    public String updateProduct(@ModelAttribute Product product, @RequestParam String categoryName,
            HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isSeller(session))
            return "redirect:/login";
        User seller = (User) session.getAttribute("user");

        try {
            Product existingProduct = productService.getProductById(product.getId());
            if (!existingProduct.getSeller().getId().equals(seller.getId())) {
                throw new RuntimeException("Unauthorized");
            }

            if (!existingProduct.getSeller().getId().equals(seller.getId())) {
                throw new RuntimeException("Unauthorized");
            }

            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setDiscountedPrice(product.getDiscountedPrice());
            existingProduct.setQuantity(product.getQuantity());
            existingProduct.setImageUrl(product.getImageUrl());
            existingProduct.setCategory(Product.Category.valueOf(categoryName));
            existingProduct.setUpdatedAt(LocalDateTime.now());

            productService.updateProduct(existingProduct);
            redirectAttributes.addFlashAttribute("msg", "Product updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update product.");
        }
        return "redirect:/seller/inventory";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isSeller(session))
            return "redirect:/login";
        User seller = (User) session.getAttribute("user");

        try {
            Product product = productService.getProductById(id);
            if (!product.getSeller().getId().equals(seller.getId())) {
                throw new RuntimeException("Unauthorized");
            }
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("msg", "Product deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Failed to delete product (It might be associated with orders).");
        }
        return "redirect:/seller/inventory";
    }

    // --- Order Management ---
    @GetMapping("/orders")
    public String showSellerOrders(HttpSession session, Model model) {
        if (!isSeller(session))
            return "redirect:/login";
        User seller = (User) session.getAttribute("user");

        List<Product> products = productService.getProductsBySeller(seller.getId());

        List<OrderItem> sellerOrderItems = new ArrayList<>();
        for (Product product : products) {
            sellerOrderItems.addAll(orderItemRepo.findByProduct(product));
        }

        // Sort by order date desc
        sellerOrderItems.sort((oi1, oi2) -> oi2.getOrder().getCreatedAt().compareTo(oi1.getOrder().getCreatedAt()));

        model.addAttribute("sellerOrderItems", sellerOrderItems);
        return "seller/seller-orders";
    }

    @PostMapping("/order/updateStatus")
    public String updateOrderStatus(@RequestParam Long orderId, @RequestParam String status,
            HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isSeller(session))
            return "redirect:/login";

        try {
            orderService.updateOrderStatus(orderId, status);
            redirectAttributes.addFlashAttribute("msg", "Order status updated to " + status);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update order status.");
        }
        return "redirect:/seller/orders";
    }

    private boolean isSeller(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && user.getRole() == User.Role.SELLER;
    }
}
