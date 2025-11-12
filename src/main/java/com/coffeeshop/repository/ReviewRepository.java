package com.coffeeshop.repository;

import com.coffeeshop.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Review entity operations.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Find reviews by product ID.
     */
    Page<Review> findByProductId(Long productId, Pageable pageable);

    /**
     * Find reviews by user ID.
     */
    List<Review> findByUserId(Long userId);

    /**
     * Find verified purchase reviews for product.
     */
    Page<Review> findByProductIdAndVerifiedPurchaseTrue(Long productId, Pageable pageable);

    /**
     * Check if user has reviewed a product.
     */
    boolean existsByUserIdAndProductId(Long userId, Long productId);

    /**
     * Find user's review for a product.
     */
    Optional<Review> findByUserIdAndProductId(Long userId, Long productId);

    /**
     * Calculate average rating for product.
     */
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double calculateAverageRating(@Param("productId") Long productId);

    /**
     * Count reviews for product.
     */
    long countByProductId(Long productId);
}
