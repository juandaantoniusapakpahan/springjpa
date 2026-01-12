package com.backend.springjpa.service;


import com.backend.springjpa.dto.ReviewDto;
import com.backend.springjpa.entity.OrderItem;
import com.backend.springjpa.entity.ProductVariant;
import com.backend.springjpa.entity.Review;
import com.backend.springjpa.entity.User;
import com.backend.springjpa.exception.ResourceNotFoundException;
import com.backend.springjpa.repository.OrderItemRepository;
import com.backend.springjpa.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {

    private final UserService userService;
    private final ProductVariantService productVariantService;
    private final ReviewRepository reviewRepository;
    private final OrderItemService orderItemService;

    @Value("${review.expiration-time}")
    private Long expirationTime;

    public ReviewService(UserService userService, ProductVariantService productVariantService,
                         ReviewRepository reviewRepository,
                         OrderItemService orderItemService) {
        this.userService = userService;
        this.productVariantService = productVariantService;
        this.reviewRepository = reviewRepository;
        this.orderItemService = orderItemService;
    }

    @Transactional
    public void upsertReview(ReviewDto reviewDto) {
        Long reviewId = Long.parseLong(reviewDto.getReviewId());
        int rating = Integer.parseInt(reviewDto.getRating());
        Review review = reviewRepository.findById(reviewId).orElseThrow(()-> new ResourceNotFoundException("Review not found"));

        review.setRating(rating);
        review.setComment(reviewDto.getComment());
        review.setUpdated(true);
        reviewRepository.save(review);
    }

    @Transactional
    public void upsertReviewPaidOrder(List<List<Long>> userAndVariantId) {
        List<Review> reviews = new ArrayList<>();
        for (List<Long> ids : userAndVariantId) {
            Long orderItemId = ids.get(0);
            Long userId = ids.get(1);
            Long productVariantId = ids.get(2);

            OrderItem orderItem = orderItemService.getByOrderItemId(orderItemId);
            User user = userService.getUserById(userId);
            ProductVariant productVariant = productVariantService.getProductVariantById(productVariantId);
            Review review = reviewRepository.findByUserIdAndProductVariantId(userId, productVariantId).orElseGet(Review::new);

            review.setOrderItem(orderItem);
            review.setUser(user);
            review.setProductVariant(productVariant);
            review.setRating(0);
            review.setComment("");
            review.setUpdated(false);
            reviews.add(review);
            review.setExpiredAt(LocalDateTime.now().plusDays(expirationTime));
        }
        reviewRepository.saveAll(reviews);
    }
}
