package com.coffeeshop.enums;

/**
 * Order status lifecycle.
 */
public enum OrderStatus {
    PENDING,           // Order received
    CONFIRMED,         // Order confirmed by shop
    PREPARING,         // Being prepared
    READY,            // Ready for pickup/delivery
    OUT_FOR_DELIVERY, // Out for delivery
    DELIVERED,        // Successfully delivered
    CANCELLED,        // Cancelled by user or system
    FAILED           // Failed/rejected
}
