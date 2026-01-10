package com.backend.springjpa.service;


import com.backend.springjpa.dto.ReviewDto;
import com.backend.springjpa.entity.ProductVariant;
import com.backend.springjpa.entity.Review;
import com.backend.springjpa.entity.User;
import com.backend.springjpa.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final UserService userService;
    private final ProductVariantService productVariantService;
    private final ReviewRepository reviewRepository;

    public ReviewService(UserService userService, ProductVariantService productVariantService, ReviewRepository reviewRepository) {
        this.userService = userService;
        this.productVariantService = productVariantService;
        this.reviewRepository = reviewRepository;
    }

    public void upsertReview(ReviewDto reviewDto) {
        Long userId = Long.parseLong(reviewDto.getUserId());
        Long productVariantId = Long.parseLong(reviewDto.getProductVariantId());
        int rating = Integer.parseInt(reviewDto.getRating());

        User user = userService.getUserById(userId);
        ProductVariant productVariant = productVariantService.getProductVariantById(productVariantId);

        Review review = reviewRepository.findByUserIdAndProductVariantId(userId, productVariantId).orElseGet(Review::new);
        review.setUser(user);
        review.setProductVariant(productVariant);
        review.setRating(rating);
        review.setComment(reviewDto.getComment());

        reviewRepository.save(review);
    }
}
