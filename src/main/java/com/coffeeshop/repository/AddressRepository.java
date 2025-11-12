package com.coffeeshop.repository;

import com.coffeeshop.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Address entity operations.
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    /**
     * Find addresses by user ID.
     */
    List<Address> findByUserId(Long userId);

    /**
     * Find default address for user.
     */
    Optional<Address> findByUserIdAndIsDefaultTrue(Long userId);

    /**
     * Find addresses by user ID and label.
     */
    Optional<Address> findByUserIdAndLabel(Long userId, String label);
}
