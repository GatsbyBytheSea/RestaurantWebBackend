package com.sunnyserenade.midnightdiner.dto.request;

public class OrderItemRequest {
    private Long dishId;
    private Integer quantity;

    // Getters and Setters
    public Long getDishId() { return dishId; }
    public void setDishId(Long dishId) { this.dishId = dishId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}