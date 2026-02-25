package com.rev.app.service.Impl;

import com.rev.app.entity.Product;
import com.rev.app.entity.Review;
import com.rev.app.entity.User;
import com.rev.app.repository.IProductRepository;
import com.rev.app.repository.IReviewRepository;
import com.rev.app.repository.IUserRepository;
import com.rev.app.service.Interface.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements IReviewService {

    @Autowired
    private IReviewRepository reviewRepo;
    @Autowired
    private IUserRepository userRepo;
    @Autowired
    private IProductRepository productRepo;

    @Override
    public Review addReview(Review review, Long userId, Long productId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        review.setUser(user);
        review.setProduct(product);
        return reviewRepo.save(review);
    }

    @Override
    public List<Review> getReviewsByProductId(Long productId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        return reviewRepo.findByProduct(product);
    }
}
