package com.rev.app.repository;

import com.rev.app.entity.Cart;
import com.rev.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByUser(User user);
}
