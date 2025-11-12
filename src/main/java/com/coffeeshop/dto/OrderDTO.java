package com.coffeeshop.dto;

import com.coffeeshop.enums.CoffeeSize;
import com.coffeeshop.enums.OrderStatus;
import com.coffeeshop.enums.PaymentMethod;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTOs for Order operations.
 */
public class OrderDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderResponse {
        private Long id;
        private String orderNumber;
        private LocalDateTime orderDate;
        private OrderStatus status;
        private BigDecimal subtotal;
        private BigDecimal tax;
        private BigDecimal deliveryFee;
        private BigDecimal totalAmount;
        private PaymentMethod paymentMethod;
        private Boolean paid;
        private AddressDTO.AddressResponse deliveryAddress;
        private List<OrderItemResponse> items;
        private LocalDateTime estimatedDeliveryTime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOrderRequest {
        @NotNull(message = "Delivery address is required")
        private Long deliveryAddressId;

        @NotNull(message = "Payment method is required")
        private PaymentMethod paymentMethod;

        @NotEmpty(message = "Order must contain at least one item")
        private List<OrderItemRequest> items;

        @Size(max = 500, message = "Special instructions must not exceed 500 characters")
        private String specialInstructions;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequest {
        @NotNull(message = "Product ID is required")
        private Long productId;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;

        private CoffeeSize size;

        @Size(max = 500, message = "Customizations must not exceed 500 characters")
        private String customizations;

        @Size(max = 500, message = "Notes must not exceed 500 characters")
        private String notes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemResponse {
        private Long id;
        private Long productId;
        private String productName;
        private String productImage;
        private Integer quantity;
        private BigDecimal price;
        private CoffeeSize size;
        private String customizations;
        private String notes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateOrderStatusRequest {
        @NotNull(message = "Status is required")
        private OrderStatus status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderSummaryResponse {
        private Long id;
        private String orderNumber;
        private LocalDateTime orderDate;
        private OrderStatus status;
        private BigDecimal totalAmount;
        private int itemCount;
    }
}
