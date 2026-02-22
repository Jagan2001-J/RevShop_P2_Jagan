package com.rev.app.repository;

import com.rev.app.entity.Cart;
import com.rev.app.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICartItemRepository extends JpaRepository<CartItem,Integer> {
    List<CartItem> findByCart(Cart cart);
}
