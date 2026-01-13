package com.backend.springjpa.service;


import com.backend.springjpa.dto.ReviewDto;
import com.backend.springjpa.entity.OrderItem;
import com.backend.springjpa.entity.ProductVariant;
import com.backend.springjpa.entity.Review;
import com.backend.springjpa.entity.User;
import com.backend.springjpa.exception.ResourceNotFoundException;
import com.backend.springjpa.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
    public void upsertReviewPaidOrder(List<long[]> userAndVariantOrderItemId) {
        List<Review> reviews = new ArrayList<>();
        for (long[] ids : userAndVariantOrderItemId) {
            Long orderItemId = ids[0];
            Long userId = ids[1];
            Long productVariantId = ids[2];

            Review review = reviewRepository.findByOrderItemId(orderItemId).orElseGet(Review::new);
            OrderItem orderItem = orderItemService.getByOrderItemId(orderItemId);
            User user = userService.getUserById(userId);
            ProductVariant productVariant = productVariantService.getProductVariantById(productVariantId);

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

    @Scheduled(fixedRateString = "${review.scheduler.review-non-updated}")
    @Transactional
    public void setRateNonUpdated() {
        reviewRepository.updateNonUpdatedReview(LocalDateTime.now());
    }
}
