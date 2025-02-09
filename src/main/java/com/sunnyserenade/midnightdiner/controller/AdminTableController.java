package com.sunnyserenade.midnightdiner.controller;

import com.sunnyserenade.midnightdiner.entity.RestaurantTable;
import com.sunnyserenade.midnightdiner.service.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller for managing operations related to restaurant tables,
 * including creation, updating status, and deletion.
 */
@RestController
@RequestMapping("/api/v1/admin/tables")
public class AdminTableController {

    /**
     * Service for handling table-related business logic.
     */
    @Autowired
    private RestaurantTableService tableService;

    /**
     * Retrieves all available tables from the restaurant.
     *
     * @return a list of RestaurantTable objects
     */
    @GetMapping
    public List<RestaurantTable> getAllTables() {
        return tableService.findAllTables();
    }

    /**
     * Updates the status of a specific table.
     *
     * @param id   the ID of the table to update
     * @param body a map containing the new status (e.g., "occupied", "available")
     * @return a ResponseEntity containing the updated table entity
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<RestaurantTable> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        String status = body.get("status");
        RestaurantTable updated = tableService.updateTableStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    /**
     * Adds a new table to the system.
     *
     * @param table the table entity to be created
     * @return a ResponseEntity containing the newly created table
     */
    @PostMapping
    public ResponseEntity<RestaurantTable> addTable(@RequestBody RestaurantTable table) {
        RestaurantTable saved = tableService.addTable(table);
        return ResponseEntity.ok(saved);
    }

    /**
     * Deletes a table from the system.
     *
     * @param id the ID of the table to delete
     * @return a ResponseEntity indicating no content (204) if successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable Long id) {
        tableService.deleteTable(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates the details of an existing table.
     *
     * @param id      the ID of the table to update
     * @param updated the table data to be updated
     * @return a ResponseEntity containing the updated table
     */
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantTable> updateTable(
            @PathVariable Long id,
            @RequestBody RestaurantTable updated
    ) {
        RestaurantTable result = tableService.updateTable(id, updated);
        return ResponseEntity.ok(result);
    }

    /**
     * Retrieves all currently available (unoccupied) tables.
     *
     * @return a list of available tables
     */
    @GetMapping("/available")
    public List<RestaurantTable> getAvailableTables() {
        return tableService.getAvailableTables();
    }
}
