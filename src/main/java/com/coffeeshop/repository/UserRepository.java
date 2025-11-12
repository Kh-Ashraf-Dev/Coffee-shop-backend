package com.coffeeshop.repository;

import com.coffeeshop.entity.User;
import com.coffeeshop.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by phone number.
     */
    Optional<User> findByPhoneNumber(String phoneNumber);

    /**
     * Check if email exists.
     */
    boolean existsByEmail(String email);

    /**
     * Check if phone number exists.
     */
    boolean existsByPhoneNumber(String phoneNumber);

    /**
     * Find users by role.
     */
    List<User> findByRole(UserRole role);

    /**
     * Find active users.
     */
    List<User> findByEnabledTrue();

    /**
     * Search users by name or email.
     */
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<User> searchUsers(@Param("searchTerm") String searchTerm);
}
