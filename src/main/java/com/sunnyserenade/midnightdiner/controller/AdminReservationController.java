package com.sunnyserenade.midnightdiner.controller;

import com.sunnyserenade.midnightdiner.entity.Reservation;
import com.sunnyserenade.midnightdiner.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/reservations")
public class AdminReservationController {

    @Autowired
    private ReservationService reservationService;

    // 管理员可查看所有预订
    @GetMapping
    public List<Reservation> getAll() {
        return reservationService.getAll();
    }

    // 管理员可根据ID获取预订
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    // 管理员可根据手机号、邮箱、用户姓名获取预定
    @GetMapping("/phone/{phone}")
    public List<Reservation> getReservationsByCustomerPhone(@PathVariable String phone) {
        return reservationService.getReservationsByCustomerPhone(phone);
    }

    @GetMapping("/name/{customerName}")
    public List<Reservation> getReservationsByCustomerName(@PathVariable String customerName) {
        return reservationService.getReservationsByCustomerName(customerName);
    }

    // 管理员可根据状态获取预定
    @GetMapping("/status/{status}")
    public List<Reservation> getReservationsByStatus(@PathVariable String status) {
        return reservationService.getReservationsByStatus(status);
    }

    // 管理员创建预订
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        Reservation saved = reservationService.createReservation(reservation);
        return ResponseEntity.ok(saved);
    }

    // 管理员更新预订
    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(
            @PathVariable Long id,
            @RequestBody Reservation updated
    ) {
        Reservation result = reservationService.updateReservation(id, updated);
        return ResponseEntity.ok(result);
    }

    // 管理员取消预订
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }

    // 管理员确认预订
    @PutMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmReservation(@PathVariable Long id) {
        reservationService.confirmReservation(id);
        return ResponseEntity.noContent().build();
    }

    // 管理员获取今日预定
    @GetMapping("/today")
    public List<Reservation> getTodayReservations() {
        return reservationService.getTodayReservations();
    }
}
