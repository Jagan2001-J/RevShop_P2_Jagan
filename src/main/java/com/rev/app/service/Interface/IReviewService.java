package com.rev.app.service.Interface;

import com.rev.app.entity.Review;
import java.util.List;

public interface IReviewService {
    Review addReview(Review review, Long userId, Long productId);

    List<Review> getReviewsByProductId(Long productId);
}
