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

    // todo: 添加方法：createTable, deleteTable
}
