package com.rev.app.mapper;

import com.rev.app.dto.ReviewRequestDTO;
import com.rev.app.dto.ReviewResponseDTO;
import com.rev.app.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review toEntity(ReviewRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Review.builder()
                .rating(dto.getRating())
                .comment(dto.getComment())
                .build();
    }

    public ReviewResponseDTO toDto(Review review) {
        if (review == null) {
            return null;
        }

        String buyerName = review.getUser() != null ? review.getUser().getName() : "Anonymous";

        return ReviewResponseDTO.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .buyerName(buyerName)
                .createdAt(review.getCreatedAt())
                .build();
    }
}
