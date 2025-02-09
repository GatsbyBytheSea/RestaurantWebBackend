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

    public List<RestaurantTable> findAllTables() {
        return tableRepository.findAll();
    }

    public RestaurantTable updateTableStatus(Long id, String status) {
        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found"));
        table.setStatus(status);
        table.setUpdateTime(LocalDateTime.now());
        return tableRepository.save(table);
    }

    public RestaurantTable addTable(RestaurantTable table) {
        table.setCreateTime(LocalDateTime.now());
        table.setUpdateTime(LocalDateTime.now());
        table.setStatus("AVAILABLE");
        return tableRepository.save(table);
    }

    public void deleteTable(Long id) {
        tableRepository.deleteById(id);
    }

    public RestaurantTable updateTable(Long id, RestaurantTable updated) {
        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found"));
        table.setStatus(updated.getStatus());
        table.setLocation(updated.getLocation());
        table.setCapacity(updated.getCapacity());
        table.setUpdateTime(LocalDateTime.now());
        table.setGridX(updated.getGridX());
        table.setGridY(updated.getGridY());
        table.setGridWidth(updated.getGridWidth());
        table.setGridHeight(updated.getGridHeight());
        table.setUpdateTime(LocalDateTime.now());
        return tableRepository.save(table);
    }

    public List<RestaurantTable> getAvailableTables() {
        return tableRepository.findByStatus("AVAILABLE");
    }

    public RestaurantTable getTable(Long id) {
        return tableRepository.findById(id).orElseThrow(() -> new RuntimeException("Table not found"));
    }
}
