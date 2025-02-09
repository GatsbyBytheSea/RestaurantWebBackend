package com.sunnyserenade.midnightdiner.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "restaurant_table")
public class RestaurantTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tableName;
    private Integer capacity;
    private String status;
    private String location;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long currentOrderId;

    @Column(name = "grid_x", nullable = false)
    private Integer gridX;
    @Column(name = "grid_y", nullable = false)
    private Integer gridY;
    @Column(name = "grid_width", nullable = false)
    private Integer gridWidth;
    @Column(name = "grid_height", nullable = false)
    private Integer gridHeight;

    // Getters/Setters

    public void setId(Long id) {
        this.id = id;
    }
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public Long getCurrentOrderId() { return currentOrderId;}
    public Integer getGridX() { return gridX; }
    public Integer getGridY() { return gridY; }
    public Integer getGridWidth() { return gridWidth; }
    public Integer getGridHeight() { return gridHeight; }

    public Integer getCapacity() {
        return capacity;
    }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public LocalDateTime getCreateTime() { return createTime; }
    public String getStatus() {
        return status;
    }
    public Long getId() {
        return id;
    }
    public String getTableName(){
        return tableName;
    }
    public String getLocation() {
        return location;
    }
    public void setCurrentOrderId(Long currentOrderId) { this.currentOrderId = currentOrderId; }
    public void setGridX(Integer gridX) { this.gridX = gridX; }
    public void setGridY(Integer gridY) { this.gridY = gridY; }
    public void setGridWidth(Integer gridWidth) { this.gridWidth = gridWidth; }
    public void setGridHeight(Integer gridHeight) { this.gridHeight = gridHeight; }
}
