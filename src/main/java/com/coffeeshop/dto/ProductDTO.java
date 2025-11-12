package com.coffeeshop.dto;

import com.coffeeshop.enums.ProductCategory;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTOs for Product operations.
 */
public class ProductDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductResponse {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private String imageUrl;
        private ProductCategory category;
        private Boolean available;
        private Boolean featured;
        private BigDecimal rating;
        private Integer reviewCount;
        private Integer prepTimeMinutes;
        private Integer calories;
        private LocalDateTime createdAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateProductRequest {
        @NotBlank(message = "Product name is required")
        @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
        private String name;

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        private String description;

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", message = "Price must be greater than 0")
        private BigDecimal price;

        private String imageUrl;

        @NotNull(message = "Category is required")
        private ProductCategory category;

        private Integer prepTimeMinutes;
        private Integer calories;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateProductRequest {
        private String name;
        private String description;
        private BigDecimal price;
        private String imageUrl;
        private ProductCategory category;
        private Boolean available;
        private Boolean featured;
        private Integer prepTimeMinutes;
        private Integer calories;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductDetailResponse {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private String imageUrl;
        private ProductCategory category;
        private Boolean available;
        private Boolean featured;
        private BigDecimal rating;
        private Integer reviewCount;
        private Integer prepTimeMinutes;
        private Integer calories;
        private LocalDateTime createdAt;
    }
}
