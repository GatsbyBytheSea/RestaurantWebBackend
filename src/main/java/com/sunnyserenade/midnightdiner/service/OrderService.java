package com.sunnyserenade.midnightdiner.service;

import com.sunnyserenade.midnightdiner.dto.request.CloseOrderRequest;
import com.sunnyserenade.midnightdiner.dto.request.OrderItemRequest;
import com.sunnyserenade.midnightdiner.entity.Dish;
import com.sunnyserenade.midnightdiner.entity.Order;
import com.sunnyserenade.midnightdiner.entity.OrderItem;
import com.sunnyserenade.midnightdiner.repository.OrderItemRepository;
import com.sunnyserenade.midnightdiner.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class OrderService {
    @Autowired private OrderRepository orderRepo;
    @Autowired private OrderItemRepository orderItemRepo;
    @Autowired private RestaurantTableService tableService;
    @Autowired private DishService dishService;
    @Autowired private DailySalesService dailySalesService;


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
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setDish(dish);
            item.setQuantity(request.getQuantity());
            item.setPrice(dish.getPrice());
            orderItems.add(item);
        }

        orderItemRepo.saveAll(orderItems);

        updateTotalAmount(order);
        return order;
    }

    private void updateTotalAmount(Order order) {
        BigDecimal total = orderItemRepo.sumPriceByOrderId(order.getId());
        order.setTotalAmount(total != null ? total : BigDecimal.ZERO);
        orderRepo.save(order);
    }

    public Page<Order> getOrdersByStatus(String status, Pageable pageable) {
        if (status != null) {
            return orderRepo.findByStatus(status, pageable);
        }
        return orderRepo.findAll(pageable);
    }

    public Order getOrderById(Long orderId) {
        return orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }

    @Transactional
    public Order closeOrder(Long orderId, CloseOrderRequest request) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if ("CLOSED".equals(order.getStatus())) {
            throw new RuntimeException("Order is already closed");
        }

        order.setStatus("CLOSED");
        order.setCloseTime(LocalDateTime.now());
        orderRepo.save(order);

        Long tableId = (request != null && request.getTableId() != null) ?
                request.getTableId() : order.getTable().getId();
        tableService.updateTableStatus(tableId, "AVAILABLE");

        dailySalesService.recordDailySales(order.getTotalAmount());

        return order;
    }
}