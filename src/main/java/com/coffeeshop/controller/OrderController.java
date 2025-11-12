package com.coffeeshop.controller;

import com.coffeeshop.dto.OrderDTO;
import com.coffeeshop.entity.User;
import com.coffeeshop.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for order operations.
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "Orders", description = "Order management APIs")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create a new order")
    public ResponseEntity<OrderDTO.OrderResponse> createOrder(
            Authentication authentication,
            @Valid @RequestBody OrderDTO.CreateOrderRequest request) {
        User user = (User) authentication.getPrincipal();
        OrderDTO.OrderResponse order = orderService.createOrder(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<OrderDTO.OrderResponse> getOrderById(
            @PathVariable Long id,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        OrderDTO.OrderResponse order = orderService.getOrderById(id, user.getId());
        return ResponseEntity.ok(order);
    }

    @GetMapping
    @Operation(summary = "Get user orders")
    public ResponseEntity<Page<OrderDTO.OrderSummaryResponse>> getUserOrders(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        User user = (User) authentication.getPrincipal();
        Page<OrderDTO.OrderSummaryResponse> orders = 
            orderService.getUserOrders(user.getId(), page, size);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/active")
    @Operation(summary = "Get active orders")
    public ResponseEntity<List<OrderDTO.OrderResponse>> getActiveOrders(
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<OrderDTO.OrderResponse> orders = orderService.getActiveOrders(user.getId());
        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update order status")
    public ResponseEntity<OrderDTO.OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderDTO.UpdateOrderStatusRequest request) {
        OrderDTO.OrderResponse order = orderService.updateOrderStatus(id, request);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel order")
    public ResponseEntity<OrderDTO.OrderResponse> cancelOrder(
            @PathVariable Long id,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        OrderDTO.OrderResponse order = orderService.cancelOrder(id, user.getId());
        return ResponseEntity.ok(order);
    }
}
