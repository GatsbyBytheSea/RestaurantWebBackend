package com.sunnyserenade.midnightdiner.controller;

import com.sunnyserenade.midnightdiner.entity.RestaurantTable;
import com.sunnyserenade.midnightdiner.service.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/tables")
public class AdminTableController {

    @Autowired
    private RestaurantTableService tableService;

    @GetMapping
    public List<RestaurantTable> getAllTables() {
        return tableService.findAllTables();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<RestaurantTable> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        String status = body.get("status");
        RestaurantTable updated = tableService.updateTableStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @PostMapping
    public ResponseEntity<RestaurantTable> addTable(@RequestBody RestaurantTable table) {
        RestaurantTable saved = tableService.addTable(table);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable Long id) {
        tableService.deleteTable(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantTable> updateTable(
            @PathVariable Long id,
            @RequestBody RestaurantTable updated
    ) {
        RestaurantTable result = tableService.updateTable(id, updated);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/available")
    public List<RestaurantTable> getAvailableTables() {
        return tableService.getAvailableTables();
    }
}
