package com.rev.app.repository;

import com.rev.app.entity.Seller;
import com.rev.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findByUser(User user);
}
