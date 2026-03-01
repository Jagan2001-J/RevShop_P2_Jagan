package com.rev.app.mapper;

import com.rev.app.dto.CartItemResponseDTO;
import com.rev.app.dto.CartResponseDTO;
import com.rev.app.entity.Cart;
import com.rev.app.entity.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartItemResponseDTO toDto(CartItem item) {
        if (item == null) {
            return null;
        }

        BigDecimal subTotal = item.getProduct().getDiscountedPrice() != null
                ? item.getProduct().getDiscountedPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                : item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

        return CartItemResponseDTO.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .price(item.getProduct().getDiscountedPrice() != null ? item.getProduct().getDiscountedPrice()
                        : item.getProduct().getPrice())
                .quantity(item.getQuantity())
                .subTotal(subTotal)
                .build();
    }

    public CartResponseDTO toDto(Cart cart) {
        if (cart == null) {
            return null;
        }

        List<CartItemResponseDTO> items = cart.getItems() != null
                ? cart.getItems().stream().map(this::toDto).collect(Collectors.toList())
                : List.of();

        BigDecimal cartTotal = items.stream()
                .map(CartItemResponseDTO::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponseDTO.builder()
                .id(cart.getId())
                .items(items)
                .cartTotal(cartTotal)
                .build();
    }
}
