package com.backend.springjpa.mapper;

import com.backend.springjpa.dto.ReviewDto;
import com.backend.springjpa.entity.Review;

public class ReviewMapper {

    public static ReviewDto entityToDto(Review review){
        ReviewDto dto = new ReviewDto();
        dto.setReviewId(String.valueOf(review.getId()));
        dto.setRating(String.valueOf(review.getRating()));
        dto.setComment(review.getComment());
        dto.setUserId(String.valueOf(review.getUser().getId()));
        dto.setOrderItemId(String.valueOf(review.getOrderItem().getId()));
        return dto;
    }
}
