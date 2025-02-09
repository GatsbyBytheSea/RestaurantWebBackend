package com.sunnyserenade.midnightdiner.service;

import com.sunnyserenade.midnightdiner.dto.request.CloseOrderRequest;
import com.sunnyserenade.midnightdiner.dto.request.CreateOrderRequest;
import com.sunnyserenade.midnightdiner.dto.request.OrderItemRequest;
import com.sunnyserenade.midnightdiner.entity.Dish;
import com.sunnyserenade.midnightdiner.entity.Order;
import com.sunnyserenade.midnightdiner.entity.OrderItem;
import com.sunnyserenade.midnightdiner.entity.RestaurantTable;
import com.sunnyserenade.midnightdiner.repository.OrderItemRepository;
import com.sunnyserenade.midnightdiner.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service handling operations related to orders, including creation,
 * adding items, closing orders, and calculating sales.
 */
@Service
public class OrderService {

    /**
     * Repository for order entity operations.
     */
    @Autowired private OrderRepository orderRepo;

    /**
     * Repository for order item entity operations.
     */
    @Autowired private OrderItemRepository orderItemRepo;

    /**
     * Service for managing restaurant tables.
     */
    @Autowired private RestaurantTableService tableService;

    /**
     * Service for handling dish retrieval and updates.
     */
    @Autowired private DishService dishService;

    /**
     * Service for recording daily sales.
     */
    @Autowired private DailySalesService dailySalesService;


    /**
     * Adds multiple items to an existing order.
     *
     * @param orderId the ID of the order to update
     * @param items   the list of items to add
     * @return the updated order
     * @throws RuntimeException if the order or any dish is not found
     */
    @Transactional
    public Order addItems(Long orderId, List<OrderItemRequest> items) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest request : items) {
            Dish dish = dishService.getDish(request.getDishId());
            if (dish == null) {
                throw new RuntimeException("Dish not found with id: " + request.getDishId());
            }
            // Build a new OrderItem object
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setDish(dish);
            item.setQuantity(request.getQuantity());
            item.setPrice(dish.getPrice());
            item.setCreateTime(LocalDateTime.now());
            orderItems.add(item);
        }

        // Update the order's update time
        order.setUpdateTime(LocalDateTime.now());
        orderRepo.save(order);

        // Save the new order items in bulk
        orderItemRepo.saveAll(orderItems);

        // Recalculate the total order amount
        updateTotalAmount(order);
        return order;
    }

    /**
     * Helper method to update an order's total amount based on its items.
     *
     * @param order the order whose total is being updated
     */
    private void updateTotalAmount(Order order) {
        BigDecimal total = orderItemRepo.sumPriceByOrderId(order.getId());
        order.setTotalAmount(total != null ? total : BigDecimal.ZERO);
        orderRepo.save(order);
    }

    /**
     * Retrieves orders, optionally filtered by status, in a paginated format.
     *
     * @param status   the desired status to filter by (optional)
     * @param pageable pagination and sorting information
     * @return a page of orders matching the criteria
     */
    public Page<Order> getOrdersByStatus(String status, Pageable pageable) {
        if (status != null) {
            return orderRepo.findByStatus(status, pageable);
        }
        return orderRepo.findAll(pageable);
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId the ID of the order to retrieve
     * @return the found order
     * @throws RuntimeException if no order is found
     */
    public Order getOrderById(Long orderId) {
        return orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }

    /**
     * Closes an existing order, making its table available again and recording the sale.
     *
     * @param orderId the ID of the order to close
     * @param request optional closing request containing additional info (e.g. table ID)
     * @return the closed order
     * @throws RuntimeException if the order is not found or is already closed
     */
    @Transactional
    public Order closeOrder(Long orderId, CloseOrderRequest request) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if ("CLOSED".equals(order.getStatus())) {
            throw new RuntimeException("Order is already closed");
        }

        // Close the order
        order.setStatus("CLOSED");
        order.setCloseTime(LocalDateTime.now());
        orderRepo.save(order);

        // Free up the table
        Long tableId = (request != null && request.getTableId() != null)
                ? request.getTableId()
                : order.getTable().getId();
        tableService.updateTableStatus(tableId, "AVAILABLE");

        // Update the table if needed
        RestaurantTable table = tableService.getTable(tableId);
        table.setCurrentOrderId(null);
        if (request != null) {
            tableService.updateTable(tableId, table);
        }

        // Record the day's sales
        dailySalesService.recordDailySales(order.getTotalAmount());

        return order;
    }

    /**
     * Creates a new order and marks the associated table as "IN_USE".
     *
     * @param request the details needed to create the order
     * @return the newly created order
     * @throws RuntimeException if the table is not available or does not exist
     */
    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        if (request.getTableId() == null) {
            throw new RuntimeException("tableId is required.");
        }

        // Check if table is valid and available
        RestaurantTable table = tableService.getTable(request.getTableId());
        if ("IN_USE".equalsIgnoreCase(table.getStatus())) {
            throw new RuntimeException("Table is not available.");
        }

        // Create the order
        Order order = new Order();
        order.setTable(table);
        order.setStatus("OPEN");
        order.setTotalAmount(BigDecimal.ZERO);
        order.setStartTime(LocalDateTime.now());
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        orderRepo.save(order);

        // Mark the table as in use
        tableService.updateTableStatus(table.getId(), "IN_USE");
        table.setCurrentOrderId(order.getId());
        tableService.updateTable(request.getTableId(), table);

        return order;
    }

    /**
     * Retrieves all items belonging to a specific order.
     *
     * @param orderId the ID of the order
     * @return the list of items in the order
     * @throws RuntimeException if the order is not found
     */
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        // Ensure the order exists
        orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        return orderItemRepo.findByOrderId(orderId);
    }

    /**
     * Removes an item from an order and recalculates the total.
     *
     * @param orderId the ID of the order
     * @param itemId  the ID of the item to remove
     * @throws RuntimeException if the order or item is not found or mismatched
     */
    @Transactional
    public void removeItem(Long orderId, Long itemId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        OrderItem item = orderItemRepo.findById(itemId)
                .orElseThrow(() -> new RuntimeException("OrderItem not found with id: " + itemId));

        // Ensure item belongs to the specified order
        if (!item.getOrder().getId().equals(orderId)) {
            throw new RuntimeException("This OrderItem does not belong to order " + orderId);
        }

        // Delete the item and update the total
        orderItemRepo.delete(item);
        updateTotalAmount(order);
    }

    /**
     * Retrieves a list of orders that have been closed today (based on their closeTime).
     *
     * @return a list of closed orders for the current day
     */
    public List<Order> getTodayClosedOrders() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay().minusSeconds(1);
        return orderRepo.findClosedOrdersByCloseTimeBetween(start, end);
    }

    /**
     * Retrieves the total sales of all closed orders for the current day.
     *
     * @return a {@link BigDecimal} representing today's sales total
     */
    public BigDecimal getTodaySales() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay().minusSeconds(1);
        BigDecimal total = orderRepo.sumTotalAmountByCloseTimeBetween(start, end);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Retrieves orders that were closed on a specific date (based on the local date's closeTime).
     *
     * @param date the date for which to retrieve closed orders
     * @return a list of closed orders from the specified date
     */
    public List<Order> getClosedOrdersByDate(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay().minusSeconds(1);
        return orderRepo.findClosedOrdersByCloseTimeBetween(start, end);
    }

    /**
     * Retrieves total sales for orders closed on a specific date.
     *
     * @param date the date for which to calculate sales
     * @return a {@link BigDecimal} representing the sales on that date
     */
    public BigDecimal getSalesByDate(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay().minusSeconds(1);
        BigDecimal total = orderRepo.sumTotalAmountByCloseTimeBetween(start, end);
        return total != null ? total : BigDecimal.ZERO;
    }
}
