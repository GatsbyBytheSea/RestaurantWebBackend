package com.sunnyserenade.midnightdiner.controller;

import com.sunnyserenade.midnightdiner.dto.request.CloseOrderRequest;
import com.sunnyserenade.midnightdiner.dto.request.CreateOrderRequest;
import com.sunnyserenade.midnightdiner.dto.request.OrderItemRequest;
import com.sunnyserenade.midnightdiner.entity.Order;
import com.sunnyserenade.midnightdiner.entity.OrderItem;
import com.sunnyserenade.midnightdiner.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Controller for managing orders, including creating, closing, and retrieving orders.
 */
@RestController
@RequestMapping("/api/v1/admin/orders")
public class OrderController {

    /**
     * Service layer handling order-specific business logic.
     */
    @Autowired
    private OrderService orderService;

    /**
     * Adds one or more items to an existing order.
     *
     * @param orderId the ID of the order to modify
     * @param items   a list of item requests to be added
     * @return the updated order or a BAD_REQUEST error if the operation fails
     */
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

    /**
     * Retrieves all items of a specific order.
     *
     * @param orderId the ID of the order whose items should be fetched
     * @return a list of {@link OrderItem} objects
     */
    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItem>> getOrderItems(@PathVariable Long orderId) {
        List<OrderItem> items = orderService.getOrderItemsByOrderId(orderId);
        return ResponseEntity.ok(items);
    }

    /**
     * Retrieves a paginated list of orders based on status.
     *
     * @param status the status to filter by (optional)
     * @param page   the page index (starting from 0)
     * @param size   the number of records per page
     * @return a paginated list of {@link Order} objects
     */
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

    /**
     * Retrieves the details of a specific order.
     *
     * @param orderId the ID of the order to fetch
     * @return the requested {@link Order}
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderDetails(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    /**
     * Closes an open order, optionally including additional closing details.
     *
     * @param orderId the ID of the order to close
     * @param request an optional request body containing closing details such as payment method
     * @return the closed order or a BAD_REQUEST error if the operation fails
     */
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

    /**
     * Creates a new order.
     *
     * @param request the request containing initial order details
     * @return the newly created order or a BAD_REQUEST error if creation fails
     */
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            Order newOrder = orderService.createOrder(request);
            return ResponseEntity.ok(newOrder);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Removes a specific item from an existing order.
     *
     * @param orderId the ID of the order from which to remove the item
     * @param itemId  the ID of the order item to remove
     * @return a NO_CONTENT response on success, or a BAD_REQUEST error if removal fails
     */
    @DeleteMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<?> removeItemFromOrder(
            @PathVariable Long orderId,
            @PathVariable Long itemId
    ) {
        try {
            orderService.removeItem(orderId, itemId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Retrieves all orders that have been closed today.
     *
     * @return a list of {@link Order} objects closed today
     */
    @GetMapping("/closed/today")
    public ResponseEntity<List<Order>> getTodayClosedOrders() {
        List<Order> orders = orderService.getTodayClosedOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieves the total sales for all orders closed today.
     *
     * @return a map containing a single key "sales" with the total as a {@link BigDecimal}
     */
    @GetMapping("/closed/today/sales")
    public ResponseEntity<Map<String, BigDecimal>> getTodaySales() {
        BigDecimal total = orderService.getTodaySales();
        return ResponseEntity.ok(Map.of("sales", total));
    }

    /**
     * Retrieves all closed orders for a specified date.
     *
     * @param date the date string in ISO format (e.g., 2025-02-09)
     * @return a list of {@link Order} objects closed on the specified date
     */
    @GetMapping("/closed/history")
    public ResponseEntity<List<Order>> getHistoricalClosedOrders(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<Order> orders = orderService.getClosedOrdersByDate(localDate);
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieves total sales for a specified historical date.
     *
     * @param date the date string in ISO format (e.g., 2025-02-09)
     * @return a map containing a single key "sales" with the total as a {@link BigDecimal}
     */
    @GetMapping("/closed/history/sales")
    public ResponseEntity<Map<String, BigDecimal>> getHistoricalSales(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        BigDecimal total = orderService.getSalesByDate(localDate);
        return ResponseEntity.ok(Map.of("sales", total));
    }
}
