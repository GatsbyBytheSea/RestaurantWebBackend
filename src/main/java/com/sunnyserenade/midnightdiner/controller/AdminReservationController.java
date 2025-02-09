package com.sunnyserenade.midnightdiner.controller;

import com.sunnyserenade.midnightdiner.entity.Reservation;
import com.sunnyserenade.midnightdiner.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/reservations")
public class AdminReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public List<Reservation> getAll() {
        return reservationService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping("/phone/{phone}")
    public List<Reservation> getReservationsByCustomerPhone(@PathVariable String phone) {
        return reservationService.getReservationsByCustomerPhone(phone);
    }

    @GetMapping("/name/{customerName}")
    public List<Reservation> getReservationsByCustomerName(@PathVariable String customerName) {
        return reservationService.getReservationsByCustomerName(customerName);
    }

    @GetMapping("/status/{status}")
    public List<Reservation> getReservationsByStatus(@PathVariable String status) {
        return reservationService.getReservationsByStatus(status);
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        Reservation saved = reservationService.createReservation(reservation);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(
            @PathVariable Long id,
            @RequestBody Reservation updated
    ) {
        Reservation result = reservationService.updateReservation(id, updated);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmReservation(
            @PathVariable Long id,
            @RequestBody Map<String, Long> request
    ) {
        Long tableId = request.get("tableId");
        reservationService.confirmReservation(id, tableId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/today")
    public List<Reservation> getTodayReservations() {
        return reservationService.getTodayReservations();
    }
}
