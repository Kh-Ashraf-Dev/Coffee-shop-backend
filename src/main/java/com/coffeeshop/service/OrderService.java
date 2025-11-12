package com.coffeeshop.service;

import com.coffeeshop.dto.AddressDTO;
import com.coffeeshop.dto.OrderDTO;
import com.coffeeshop.entity.*;
import com.coffeeshop.enums.OrderStatus;
import com.coffeeshop.exception.BadRequestException;
import com.coffeeshop.exception.ResourceNotFoundException;
import com.coffeeshop.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for handling order operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;

    /**
     * Create a new order.
     */
    @Transactional
    public OrderDTO.OrderResponse createOrder(Long userId, OrderDTO.CreateOrderRequest request) {
        log.info("Creating order for user ID: {}", userId);

        // Validate user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Validate delivery address
        Address deliveryAddress = addressRepository.findById(request.getDeliveryAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Delivery address not found"));

        if (!deliveryAddress.getUser().getId().equals(userId)) {
            throw new BadRequestException("Address does not belong to the user");
        }

        // Create order
        Order order = Order.builder()
                .user(user)
                .orderNumber(generateOrderNumber())
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .paymentMethod(request.getPaymentMethod())
                .deliveryAddress(deliveryAddress)
                .specialInstructions(request.getSpecialInstructions())
                .deliveryFee(BigDecimal.valueOf(2.99)) // Fixed delivery fee
                .estimatedDeliveryTime(LocalDateTime.now().plusMinutes(30))
                .build();

        // Add order items
        for (OrderDTO.OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product not found with ID: " + itemRequest.getProductId()));

            if (!product.getAvailable()) {
                throw new BadRequestException("Product is not available: " + product.getName());
            }

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .price(product.getPrice())
                    .size(itemRequest.getSize())
                    .customizations(itemRequest.getCustomizations())
                    .notes(itemRequest.getNotes())
                    .build();

            order.addOrderItem(orderItem);
        }

        // Calculate totals
        order.calculateTotals();

        // Save order
        order = orderRepository.save(order);
        log.info("Order created successfully with order number: {}", order.getOrderNumber());

        return convertToOrderResponse(order);
    }

    /**
     * Get order by ID.
     */
    @Transactional(readOnly = true)
    public OrderDTO.OrderResponse getOrderById(Long orderId, Long userId) {
        log.info("Fetching order with ID: {} for user: {}", orderId, userId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Verify order belongs to user
        if (!order.getUser().getId().equals(userId)) {
            throw new BadRequestException("Order does not belong to the user");
        }

        return convertToOrderResponse(order);
    }

    /**
     * Get orders by user with pagination.
     */
    @Transactional(readOnly = true)
    public Page<OrderDTO.OrderSummaryResponse> getUserOrders(Long userId, int page, int size) {
        log.info("Fetching orders for user ID: {}", userId);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("orderDate").descending());
        return orderRepository.findByUserId(userId, pageable)
                .map(this::convertToOrderSummary);
    }

    /**
     * Get active orders for user.
     */
    @Transactional(readOnly = true)
    public List<OrderDTO.OrderResponse> getActiveOrders(Long userId) {
        log.info("Fetching active orders for user ID: {}", userId);
        
        List<Order> activeOrders = orderRepository.findActiveOrdersByUserId(userId);
        return activeOrders.stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update order status.
     */
    @Transactional
    public OrderDTO.OrderResponse updateOrderStatus(
            Long orderId, OrderDTO.UpdateOrderStatusRequest request) {
        log.info("Updating order status for order ID: {} to {}", orderId, request.getStatus());

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setStatus(request.getStatus());

        // Set delivery time if order is delivered
        if (request.getStatus() == OrderStatus.DELIVERED) {
            order.setActualDeliveryTime(LocalDateTime.now());
        }

        order = orderRepository.save(order);
        log.info("Order status updated successfully");

        return convertToOrderResponse(order);
    }

    /**
     * Cancel order.
     */
    @Transactional
    public OrderDTO.OrderResponse cancelOrder(Long orderId, Long userId) {
        log.info("Cancelling order ID: {} for user: {}", orderId, userId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Verify order belongs to user
        if (!order.getUser().getId().equals(userId)) {
            throw new BadRequestException("Order does not belong to the user");
        }

        // Check if order can be cancelled
        if (order.getStatus() == OrderStatus.DELIVERED || 
            order.getStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException("Order cannot be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        order = orderRepository.save(order);
        
        log.info("Order cancelled successfully");
        return convertToOrderResponse(order);
    }

    /**
     * Generate unique order number.
     */
    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Convert Order to OrderResponse.
     */
    private OrderDTO.OrderResponse convertToOrderResponse(Order order) {
        List<OrderDTO.OrderItemResponse> items = order.getOrderItems().stream()
                .map(this::convertToOrderItemResponse)
                .collect(Collectors.toList());

        return OrderDTO.OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .subtotal(order.getSubtotal())
                .tax(order.getTax())
                .deliveryFee(order.getDeliveryFee())
                .totalAmount(order.getTotalAmount())
                .paymentMethod(order.getPaymentMethod())
                .paid(order.getPaid())
                .deliveryAddress(convertToAddressResponse(order.getDeliveryAddress()))
                .items(items)
                .estimatedDeliveryTime(order.getEstimatedDeliveryTime())
                .build();
    }

    /**
     * Convert OrderItem to OrderItemResponse.
     */
    private OrderDTO.OrderItemResponse convertToOrderItemResponse(OrderItem item) {
        return OrderDTO.OrderItemResponse.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .productImage(item.getProduct().getImageUrl())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .size(item.getSize())
                .customizations(item.getCustomizations())
                .notes(item.getNotes())
                .build();
    }

    /**
     * Convert Address to AddressResponse.
     */
    private AddressDTO.AddressResponse convertToAddressResponse(Address address) {
        return AddressDTO.AddressResponse.builder()
                .id(address.getId())
                .label(address.getLabel())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(address.getZipCode())
                .country(address.getCountry())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .isDefault(address.getIsDefault())
                .deliveryInstructions(address.getDeliveryInstructions())
                .build();
    }

    /**
     * Convert Order to OrderSummaryResponse.
     */
    private OrderDTO.OrderSummaryResponse convertToOrderSummary(Order order) {
        return OrderDTO.OrderSummaryResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .itemCount(order.getOrderItems().size())
                .build();
    }
}
