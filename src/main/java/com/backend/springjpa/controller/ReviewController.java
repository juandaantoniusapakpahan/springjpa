package com.backend.springjpa.controller;

import com.backend.springjpa.dto.ApiResponse;
import com.backend.springjpa.dto.ReviewDto;
import com.backend.springjpa.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/upsert")
    public ResponseEntity<ApiResponse<String>> upsertReview(@Valid @RequestBody ReviewDto reviewDto) {
        reviewService.upsertReview(reviewDto);
        return ResponseEntity.ok(ApiResponse.ok("Success", null, "/upsert"));
    }
}
