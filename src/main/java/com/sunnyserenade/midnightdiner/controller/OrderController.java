package com.sunnyserenade.midnightdiner.controller;

import com.sunnyserenade.midnightdiner.dto.request.CloseOrderRequest;
import com.sunnyserenade.midnightdiner.dto.request.OrderItemRequest;
import com.sunnyserenade.midnightdiner.entity.Order;
import com.sunnyserenade.midnightdiner.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    // 添加菜品到订单
    @PostMapping("/{orderId}/items")
    public ResponseEntity<?> addItems(
            @PathVariable Long orderId,
            @RequestBody List<OrderItemRequest> items
    ) {
        try {
            Order updatedOrder = orderService.addItems(orderId, items);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<Page<Order>> getOrders(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").descending());
        Page<Order> orders = orderService.getOrdersByStatus(status, pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderDetails(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/close")
    public ResponseEntity<?> closeOrder(
            @PathVariable Long orderId,
            @RequestBody(required = false) CloseOrderRequest request
    ) {
        try {
            Order closedOrder = orderService.closeOrder(orderId, request);
            return ResponseEntity.ok(closedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}