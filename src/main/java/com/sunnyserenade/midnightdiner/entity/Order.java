package com.sunnyserenade.midnightdiner.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "`order`") // 解决MySQL关键字冲突
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private RestaurantTable table;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime closeTime;

    // getters/setters
    public Long getId() {
        return id;
    }
    public RestaurantTable getTable() {
        return table;
    }
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    public String getStatus() {
        return status;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }
    public LocalDateTime getCloseTime() {
        return closeTime;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setTable(RestaurantTable table) {
        this.table = table;
    }
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public void setCloseTime(LocalDateTime closeTime) {
        this.closeTime = closeTime;
    }
}