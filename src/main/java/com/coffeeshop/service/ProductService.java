package com.coffeeshop.service;

import com.coffeeshop.dto.ProductDTO;
import com.coffeeshop.entity.Product;
import com.coffeeshop.enums.ProductCategory;
import com.coffeeshop.exception.ResourceNotFoundException;
import com.coffeeshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for handling product operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Get all products with pagination.
     */
    @Cacheable(value = "products", key = "#page + '-' + #size")
    @Transactional(readOnly = true)
    public Page<ProductDTO.ProductResponse> getAllProducts(int page, int size) {
        log.info("Fetching all products - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return productRepository.findByAvailableTrue(pageable)
                .map(this::convertToResponse);
    }

    /**
     * Get product by ID.
     */
    @Cacheable(value = "product", key = "#id")
    @Transactional(readOnly = true)
    public ProductDTO.ProductDetailResponse getProductById(Long id) {
        log.info("Fetching product with ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        return convertToDetailResponse(product);
    }

    /**
     * Get products by category.
     */
    @Cacheable(value = "productsByCategory", key = "#category + '-' + #page + '-' + #size")
    @Transactional(readOnly = true)
    public Page<ProductDTO.ProductResponse> getProductsByCategory(
            ProductCategory category, int page, int size) {
        log.info("Fetching products by category: {}", category);
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return productRepository.findByCategoryAndAvailableTrue(category, pageable)
                .map(this::convertToResponse);
    }

    /**
     * Get featured products.
     */
    @Cacheable(value = "featuredProducts")
    @Transactional(readOnly = true)
    public List<ProductDTO.ProductResponse> getFeaturedProducts() {
        log.info("Fetching featured products");
        return productRepository.findByFeaturedTrueAndAvailableTrue()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Search products.
     */
    @Transactional(readOnly = true)
    public Page<ProductDTO.ProductResponse> searchProducts(
            String searchTerm, int page, int size) {
        log.info("Searching products with term: {}", searchTerm);
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.searchProducts(searchTerm, pageable)
                .map(this::convertToResponse);
    }

    /**
     * Get top rated products.
     */
    @Cacheable(value = "topRatedProducts")
    @Transactional(readOnly = true)
    public List<ProductDTO.ProductResponse> getTopRatedProducts(int limit) {
        log.info("Fetching top {} rated products", limit);
        Pageable pageable = PageRequest.of(0, limit);
        return productRepository.findTopRatedProducts(pageable)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Create new product.
     */
    @CacheEvict(value = {"products", "productsByCategory", "featuredProducts"}, allEntries = true)
    @Transactional
    public ProductDTO.ProductResponse createProduct(ProductDTO.CreateProductRequest request) {
        log.info("Creating new product: {}", request.getName());
        
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .category(request.getCategory())
                .prepTimeMinutes(request.getPrepTimeMinutes())
                .calories(request.getCalories())
                .available(true)
                .featured(false)
                .build();

        product = productRepository.save(product);
        log.info("Product created successfully with ID: {}", product.getId());
        
        return convertToResponse(product);
    }

    /**
     * Update product.
     */
    @CacheEvict(value = {"products", "product", "productsByCategory", "featuredProducts"}, allEntries = true)
    @Transactional
    public ProductDTO.ProductResponse updateProduct(
            Long id, ProductDTO.UpdateProductRequest request) {
        log.info("Updating product with ID: {}", id);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if (request.getImageUrl() != null) {
            product.setImageUrl(request.getImageUrl());
        }
        if (request.getCategory() != null) {
            product.setCategory(request.getCategory());
        }
        if (request.getAvailable() != null) {
            product.setAvailable(request.getAvailable());
        }
        if (request.getFeatured() != null) {
            product.setFeatured(request.getFeatured());
        }
        if (request.getPrepTimeMinutes() != null) {
            product.setPrepTimeMinutes(request.getPrepTimeMinutes());
        }
        if (request.getCalories() != null) {
            product.setCalories(request.getCalories());
        }

        product = productRepository.save(product);
        log.info("Product updated successfully with ID: {}", product.getId());
        
        return convertToResponse(product);
    }

    /**
     * Delete product.
     */
    @CacheEvict(value = {"products", "product", "productsByCategory", "featuredProducts"}, allEntries = true)
    @Transactional
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }

        productRepository.deleteById(id);
        log.info("Product deleted successfully with ID: {}", id);
    }

    /**
     * Convert Product entity to ProductResponse DTO.
     */
    private ProductDTO.ProductResponse convertToResponse(Product product) {
        return ProductDTO.ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .category(product.getCategory())
                .available(product.getAvailable())
                .featured(product.getFeatured())
                .rating(product.getRating())
                .reviewCount(product.getReviewCount())
                .prepTimeMinutes(product.getPrepTimeMinutes())
                .calories(product.getCalories())
                .createdAt(product.getCreatedAt())
                .build();
    }

    /**
     * Convert Product entity to ProductDetailResponse DTO.
     */
    private ProductDTO.ProductDetailResponse convertToDetailResponse(Product product) {
        return ProductDTO.ProductDetailResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .category(product.getCategory())
                .available(product.getAvailable())
                .featured(product.getFeatured())
                .rating(product.getRating())
                .reviewCount(product.getReviewCount())
                .prepTimeMinutes(product.getPrepTimeMinutes())
                .calories(product.getCalories())
                .createdAt(product.getCreatedAt())
                .build();
    }
}
