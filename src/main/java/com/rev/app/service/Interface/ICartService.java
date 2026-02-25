package com.rev.app.service.Interface;

import com.rev.app.entity.Cart;
import com.rev.app.entity.CartItem;

public interface ICartService {
    void addToCart(Long userId, Long productId, Integer qty);

    Cart getCartByUserId(Long userId);

    void updateCartItemQuantity(Long cartItemId, Integer qty);

    void removeCartItem(Long cartItemId);

    void clearCart(Long userId);
}
