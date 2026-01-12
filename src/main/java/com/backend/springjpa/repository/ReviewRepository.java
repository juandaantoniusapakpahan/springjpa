package com.backend.springjpa.repository;

import com.backend.springjpa.entity.Review;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByUserIdAndProductVariantId(Long userId, Long productVariantId);
    Optional<Review> findByOrderItemId(Long orderItemId);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE reviews set updated = true, rating = 5 WHERE updated = false AND expired_at <= :now
            """, nativeQuery = true)
    void updateNonUpdatedReview(@Param("now") LocalDateTime now);
}
