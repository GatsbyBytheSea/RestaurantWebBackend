package com.sunnyserenade.midnightdiner.service;

import com.sunnyserenade.midnightdiner.entity.RestaurantTable;
import com.sunnyserenade.midnightdiner.repository.RestaurantTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RestaurantTableService {

    @Autowired
    private RestaurantTableRepository tableRepository;

    // 查看所有餐桌
    public List<RestaurantTable> findAllTables() {
        return tableRepository.findAll();
    }

    // 更新餐桌状态
    public RestaurantTable updateTableStatus(Long id, String status) {
        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found"));
        table.setStatus(status);
        table.setUpdateTime(LocalDateTime.now());
        return tableRepository.save(table);
    }

    // 添加餐桌
    public RestaurantTable addTable(RestaurantTable table) {
        table.setCreateTime(LocalDateTime.now());
        table.setUpdateTime(LocalDateTime.now());
        table.setStatus("AVAILABLE");
        return tableRepository.save(table);
    }

    // 删除餐桌
    public void deleteTable(Long id) {
        tableRepository.deleteById(id);
    }

    // 修改餐桌信息
    public RestaurantTable updateTable(Long id, RestaurantTable updated) {
        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found"));
        table.setStatus(updated.getStatus());
        table.setLocation(updated.getLocation());
        table.setCapacity(updated.getCapacity());
        table.setUpdateTime(LocalDateTime.now());
        return tableRepository.save(table);
    }

    public List<RestaurantTable> getAvailableTables() {
        return tableRepository.findByStatus("AVAILABLE");
    }
}
