package com.sunnyserenade.midnightdiner.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "restaurant_table")
public class RestaurantTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tableName;     // 桌名/编号
    private Integer capacity;     // 可容纳人数
    private String status;        // AVAILABLE / IN_USE / ...
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // ======= Getters/Setters =======

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


    public Integer getCapacity() {
        return capacity;
    }
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    public String getStatus() {
        return status;
    }
    public Long getId() {
        return id;
    }
    public String getTableName(){
        return tableName;
    }
}
