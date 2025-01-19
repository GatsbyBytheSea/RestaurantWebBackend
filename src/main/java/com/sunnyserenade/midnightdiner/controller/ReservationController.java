package com.sunnyserenade.midnightdiner.controller;

import com.sunnyserenade.midnightdiner.entity.Reservation;
import com.sunnyserenade.midnightdiner.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    /**
     * 创建预订
     * POST /api/v1/reservations
     */
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        Reservation saved = reservationService.createReservation(reservation);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * 根据ID获取预订
     * GET /api/v1/reservations/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    /**
     * 更新预订(全部字段更新)
     * PUT /api/v1/reservations/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(
            @PathVariable Long id,
            @RequestBody Reservation updated
    ) {
        Reservation result = reservationService.updateReservation(id, updated);
        return ResponseEntity.ok(result);
    }

    /**
     * 取消预订
     * DELETE /api/v1/reservations/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }
}