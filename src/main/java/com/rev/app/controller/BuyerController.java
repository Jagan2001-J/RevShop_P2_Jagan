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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class BuyerController {

    @Autowired
    private IProductService productService;
    @Autowired
    private ICartService cartService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IAddressService addressService;
    @Autowired
    private IFavoriteService favoriteService;
    @Autowired
    private IReviewService reviewService;
    @Autowired
    private IOrderItemRepository orderItemRepo;

    // --- Product Catalog ---
    @GetMapping("/products")
    public String showProducts(@RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sort,
            Model model) {
        List<Product> products = productService.getAllProducts();

        if (category != null && !category.isEmpty()) {
            products = products.stream().filter(p -> p.getCategory().name().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
        }
        if (keyword != null && !keyword.isEmpty()) {
            products = products.stream()
                    .filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                            p.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if ("price_asc".equals(sort)) {
            products.sort((p1, p2) -> Double.compare(p1.getPrice().doubleValue(), p2.getPrice().doubleValue()));
        } else if ("price_desc".equals(sort)) {
            products.sort((p1, p2) -> Double.compare(p2.getPrice().doubleValue(), p1.getPrice().doubleValue()));
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", Product.Category.values());
        return "buyer/products";
    }

    @GetMapping("/product/{id}")
    public String showProductDetails(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);

        List<Review> reviews = reviewService.getReviewsByProductId(id);
        model.addAttribute("reviews", reviews);
        model.addAttribute("newReview", new Review());

        return "buyer/product-details";
    }

    @PostMapping("/product/review")
    public String addReview(@ModelAttribute("newReview") Review review, @RequestParam Long productId,
            HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        try {
            reviewService.addReview(review, user.getId(), productId);
            redirectAttributes.addFlashAttribute("msg", "Review added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Could not add review.");
        }
        return "redirect:/product/" + productId;
    }

    // --- Cart Management ---
    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        Cart cart = cartService.getCartByUserId(user.getId());
        model.addAttribute("cart", cart);

        if (cart != null && cart.getItems() != null && !cart.getItems().isEmpty()) {
            double subtotal = cart.getItems().stream().mapToDouble(i -> {
                double price = (i.getProduct().getDiscountedPrice() != null)
                        ? i.getProduct().getDiscountedPrice().doubleValue()
                        : i.getProduct().getPrice().doubleValue();
                return price * i.getQuantity();
            }).sum();

            model.addAttribute("subtotal", String.format("%.2f", subtotal));
            model.addAttribute("total", String.format("%.2f", subtotal));
        }

        return "buyer/cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity,
            HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        try {
            cartService.addToCart(user.getId(), productId, quantity);
            redirectAttributes.addFlashAttribute("msg", "Item added to cart.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/update")
    public String updateCart(@RequestParam Long cartItemId, @RequestParam int quantity,
            HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        cartService.updateCartItemQuantity(cartItemId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long cartItemId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        cartService.removeCartItem(cartItemId);
        return "redirect:/cart";
    }

    @PostMapping("/cart/clear")
    public String clearCart(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        cartService.clearCart(user.getId());
        return "redirect:/cart";
    }

    // --- Checkout & Orders ---
    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        Cart cart = cartService.getCartByUserId(user.getId());
        if (cart == null || cart.getItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Your cart is empty.");
            return "redirect:/cart";
        }

        List<Address> addresses = addressService.getAddressesByUserId(user.getId());
        model.addAttribute("addresses", addresses);
        model.addAttribute("cart", cart);

        // Recalculate totals
        double subtotal = cart.getItems().stream().mapToDouble(i -> {
            double price = (i.getProduct().getDiscountedPrice() != null)
                    ? i.getProduct().getDiscountedPrice().doubleValue()
                    : i.getProduct().getPrice().doubleValue();
            return price * i.getQuantity();
        }).sum();

        model.addAttribute("subtotal", String.format("%.2f", subtotal));
        model.addAttribute("total", String.format("%.2f", subtotal));

        return "buyer/checkout";
    }

    @PostMapping("/checkout/process")
    public String processCheckout(@RequestParam Long shippingAddressId,
            @RequestParam(required = false) Long billingAddressId,
            @RequestParam String paymentMethod,
            HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        if (billingAddressId == null) {
            billingAddressId = shippingAddressId; // Same as shipping
        }

        try {
            orderService.placeOrder(user.getId(), shippingAddressId, billingAddressId, paymentMethod);
            redirectAttributes.addFlashAttribute("msg", "Order placed successfully!");
            return "redirect:/orders";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/checkout";
        }
    }

    @GetMapping("/orders")
    public String viewOrders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        List<Order> orders = orderService.getOrdersByUser(user.getId());
        model.addAttribute("orders", orders);

        // Fetch order items for each order
        Map<Long, List<OrderItem>> orderItemsMap = orders.stream()
                .collect(Collectors.toMap(Order::getId, orderItemRepo::findByOrder));
        model.addAttribute("orderItemsMap", orderItemsMap);

        return "buyer/orders";
    }

    // --- Favorites ---
    @PostMapping("/favorites/add")
    public String addToFavorites(@RequestParam Long productId, HttpSession session,
            RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        try {
            favoriteService.addToFavorites(user.getId(), productId);
            redirectAttributes.addFlashAttribute("msg", "Product added to wishlist!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Could not add to wishlist.");
        }
        return "redirect:/product/" + productId;
    }

    @GetMapping("/favorites")
    public String viewFavorites(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        List<Favorite> favorites = favoriteService.getFavoritesByUserId(user.getId());
        model.addAttribute("favorites", favorites);
        return "buyer/favorites";
    }

    @PostMapping("/favorites/remove")
    public String removeFromFavorites(@RequestParam Long productId, HttpSession session,
            RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        try {
            favoriteService.removeFromFavorites(user.getId(), productId);
            redirectAttributes.addFlashAttribute("msg", "Product removed from wishlist.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Could not remove from wishlist.");
        }
        return "redirect:/favorites";
    }

}
