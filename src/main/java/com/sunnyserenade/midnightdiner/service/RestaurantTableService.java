package com.sunnyserenade.midnightdiner.service;

import com.sunnyserenade.midnightdiner.entity.RestaurantTable;
import com.sunnyserenade.midnightdiner.repository.RestaurantTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for managing restaurant tables, including status updates,
 * creation, and retrieval.
 */
@Service
public class RestaurantTableService {

    /**
     * Repository for table entity operations.
     */
    @Autowired
    private RestaurantTableRepository tableRepository;

    /**
     * Retrieves all restaurant tables.
     *
     * @return a list of all {@link RestaurantTable} objects
     */
    public List<RestaurantTable> findAllTables() {
        return tableRepository.findAll();
    }

    /**
     * Updates the status of a specific table (e.g., "AVAILABLE", "RESERVED", "IN_USE").
     *
     * @param id     the ID of the table to update
     * @param status the new status
     * @return the updated table entity
     * @throws RuntimeException if no table is found with the given ID
     */
    public RestaurantTable updateTableStatus(Long id, String status) {
        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found"));
        table.setStatus(status);
        table.setUpdateTime(LocalDateTime.now());
        return tableRepository.save(table);
    }

    /**
     * Creates a new restaurant table with default status "AVAILABLE".
     *
     * @param table the table object to be saved
     * @return the newly saved table with generated fields
     */
    public RestaurantTable addTable(RestaurantTable table) {
        table.setCreateTime(LocalDateTime.now());
        table.setUpdateTime(LocalDateTime.now());
        table.setStatus("AVAILABLE");
        return tableRepository.save(table);
    }

    /**
     * Deletes a table by its ID.
     *
     * @param id the ID of the table to delete
     */
    public void deleteTable(Long id) {
        tableRepository.deleteById(id);
    }

    /**
     * Updates the details of an existing table.
     *
     * @param id      the ID of the table to update
     * @param updated the updated table details
     * @return the updated table entity
     * @throws RuntimeException if the table is not found
     */
    public RestaurantTable updateTable(Long id, RestaurantTable updated) {
        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found"));
        table.setStatus(updated.getStatus());
        table.setLocation(updated.getLocation());
        table.setCapacity(updated.getCapacity());
        // Additional fields related to layout or grid positioning
        table.setGridX(updated.getGridX());
        table.setGridY(updated.getGridY());
        table.setGridWidth(updated.getGridWidth());
        table.setGridHeight(updated.getGridHeight());
        table.setUpdateTime(LocalDateTime.now());
        return tableRepository.save(table);
    }

    /**
     * Retrieves all tables that are currently marked as "AVAILABLE".
     *
     * @return a list of available tables
     */
    public List<RestaurantTable> getAvailableTables() {
        return tableRepository.findByStatus("AVAILABLE");
    }

    /**
     * Retrieves a specific table by its ID.
     *
     * @param id the ID of the table
     * @return the found table entity
     * @throws RuntimeException if no table exists with the given ID
     */
    public RestaurantTable getTable(Long id) {
        return tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found"));
    }
}
