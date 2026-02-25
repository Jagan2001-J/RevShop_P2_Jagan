package com.rev.app.service.Impl;

import com.rev.app.entity.Cart;
import com.rev.app.entity.CartItem;
import com.rev.app.entity.Product;
import com.rev.app.entity.User;
import com.rev.app.repository.ICartItemRepository;
import com.rev.app.repository.ICartRepository;
import com.rev.app.repository.IProductRepository;
import com.rev.app.repository.IUserRepository;
import com.rev.app.service.Interface.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private ICartRepository cartRepo;
    @Autowired
    private ICartItemRepository itemRepo;
    @Autowired
    private IProductRepository productRepo;
    @Autowired
    private IUserRepository userRepo;

    @Override
    @Transactional
    public void addToCart(Long userId, Long productId, Integer qty) {
        User u = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartRepo.findByUser(u).orElseGet(() -> cartRepo.save(Cart.builder().user(u).build()));
        Product p = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        if (cart.getItems() == null) {
            cart.setItems(new java.util.ArrayList<>());
        }

        // Check if item already exists in cart
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + qty);
            itemRepo.save(existingItem);
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(p);
            item.setQuantity(qty);
            cart.getItems().add(item);
            itemRepo.save(item);
        }
        cartRepo.save(cart);
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        User u = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return cartRepo.findByUser(u).orElse(null);
    }

    @Override
    @Transactional
    public void updateCartItemQuantity(Long cartItemId, Integer qty) {
        CartItem item = itemRepo.findById(cartItemId).orElseThrow(() -> new RuntimeException("CartItem not found"));
        if (qty <= 0) {
            removeCartItem(cartItemId);
        } else {
            item.setQuantity(qty);
            itemRepo.save(item);
        }
    }

    @Override
    @Transactional
    public void removeCartItem(Long cartItemId) {
        CartItem item = itemRepo.findById(cartItemId).orElse(null);
        if (item != null) {
            Cart cart = item.getCart();
            cart.getItems().remove(item);
            cartRepo.save(cart);
        }
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        if (cart != null && cart.getItems() != null) {
            cart.getItems().clear();
            cartRepo.save(cart);
        }
    }
}
