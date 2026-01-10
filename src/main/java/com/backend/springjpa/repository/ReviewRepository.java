package com.backend.springjpa.repository;

import com.backend.springjpa.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByUserIdAndProductVariantId(Long userId, Long productVariantId);
}
