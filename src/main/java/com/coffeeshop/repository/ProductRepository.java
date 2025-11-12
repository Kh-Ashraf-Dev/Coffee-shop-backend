package com.coffeeshop.repository;

import com.coffeeshop.entity.Product;
import com.coffeeshop.enums.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Product entity operations.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find products by category.
     */
    Page<Product> findByCategory(ProductCategory category, Pageable pageable);

    /**
     * Find available products.
     */
    Page<Product> findByAvailableTrue(Pageable pageable);

    /**
     * Find featured products.
     */
    List<Product> findByFeaturedTrueAndAvailableTrue();

    /**
     * Find products by category and availability.
     */
    Page<Product> findByCategoryAndAvailableTrue(ProductCategory category, Pageable pageable);

    /**
     * Search products by name or description.
     */
    @Query("SELECT p FROM Product p WHERE " +
           "p.available = true AND (" +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Product> searchProducts(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find top rated products.
     */
    @Query("SELECT p FROM Product p WHERE p.available = true AND p.rating IS NOT NULL " +
           "ORDER BY p.rating DESC, p.reviewCount DESC")
    List<Product> findTopRatedProducts(Pageable pageable);

    /**
     * Find products by price range.
     */
    @Query("SELECT p FROM Product p WHERE p.available = true AND " +
           "p.price BETWEEN :minPrice AND :maxPrice")
    Page<Product> findByPriceRange(@Param("minPrice") java.math.BigDecimal minPrice,
                                    @Param("maxPrice") java.math.BigDecimal maxPrice,
                                    Pageable pageable);
}
