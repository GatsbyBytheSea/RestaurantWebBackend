package com.sunnyserenade.midnightdiner.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class OrderItem {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dish;

    private Integer quantity;
    private BigDecimal price;

    // getters/setters
    public Long getId() {
        return id;
    }
    public Order getOrder() {
        return order;
    }
    public Dish getDish() {
        return dish;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public BigDecimal getPrice() {
        return price;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setOrder(Order order) {
        this.order = order;
    }
    public void setDish(Dish dish) {
        this.dish = dish;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}