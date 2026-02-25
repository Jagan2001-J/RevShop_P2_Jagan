package com.rev.app.repository;

import com.rev.app.entity.Product;
import com.rev.app.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProduct(Product product);
}
