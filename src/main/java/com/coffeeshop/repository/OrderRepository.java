package com.coffeeshop.repository;

import com.coffeeshop.entity.Order;
import com.coffeeshop.entity.User;
import com.coffeeshop.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Order entity operations.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find order by order number.
     */
    Optional<Order> findByOrderNumber(String orderNumber);

    /**
     * Find orders by user.
     */
    Page<Order> findByUser(User user, Pageable pageable);

    /**
     * Find orders by user ID.
     */
    Page<Order> findByUserId(Long userId, Pageable pageable);

    /**
     * Find orders by status.
     */
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    /**
     * Find orders by user and status.
     */
    Page<Order> findByUserIdAndStatus(Long userId, OrderStatus status, Pageable pageable);

    /**
     * Find recent orders for a user.
     */
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId " +
           "ORDER BY o.orderDate DESC")
    List<Order> findRecentOrdersByUserId(@Param("userId") Long userId, Pageable pageable);

    /**
     * Find orders within date range.
     */
    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> findOrdersBetweenDates(@Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);

    /**
     * Count orders by status.
     */
    long countByStatus(OrderStatus status);

    /**
     * Find active orders for user.
     */
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND " +
           "o.status IN ('PENDING', 'CONFIRMED', 'PREPARING', 'READY', 'OUT_FOR_DELIVERY') " +
           "ORDER BY o.orderDate DESC")
    List<Order> findActiveOrdersByUserId(@Param("userId") Long userId);

    /**
     * Calculate total revenue between dates.
     */
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE " +
           "o.status = 'DELIVERED' AND " +
           "o.orderDate BETWEEN :startDate AND :endDate")
    java.math.BigDecimal calculateRevenue(@Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);
}
