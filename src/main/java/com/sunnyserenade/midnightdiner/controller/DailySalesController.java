package com.sunnyserenade.midnightdiner.controller;

import com.sunnyserenade.midnightdiner.entity.DailySales;
import com.sunnyserenade.midnightdiner.service.DailySalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/daily-sales")
public class DailySalesController {
    @Autowired
    private DailySalesService dailySalesService;

    @GetMapping
    public ResponseEntity<List<DailySales>> getDailySales(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        List<DailySales> sales = dailySalesService.getSalesBetween(start, end);
        return ResponseEntity.ok(sales);
    }
}