package com.sunnyserenade.midnightdiner.controller;

import com.sunnyserenade.midnightdiner.entity.Reservation;
import com.sunnyserenade.midnightdiner.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller for managing reservation operations, such as creation,
 * retrieval, update, and deletion.
 */
@RestController
@RequestMapping("/api/v1/admin/reservations")
public class AdminReservationController {

    /**
     * Service for handling all reservation-related business logic.
     */
    @Autowired
    private ReservationService reservationService;

    /**
     * Retrieves all reservations from the system.
     *
     * @return a list of all existing reservations
     */
    @GetMapping
    public List<Reservation> getAll() {
        return reservationService.getAll();
    }

    /**
     * Retrieves a specific reservation by its ID.
     *
     * @param id the unique identifier of the reservation
     * @return a ResponseEntity containing the reservation, or a 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    /**
     * Retrieves all reservations associated with a given customer phone number.
     *
     * @param phone the customer's phone number
     * @return a list of matching reservations
     */
    @GetMapping("/phone/{phone}")
    public List<Reservation> getReservationsByCustomerPhone(@PathVariable String phone) {
        return reservationService.getReservationsByCustomerPhone(phone);
    }

    /**
     * Retrieves all reservations under a given customer name.
     *
     * @param customerName the customer's name
     * @return a list of matching reservations
     */
    @GetMapping("/name/{customerName}")
    public List<Reservation> getReservationsByCustomerName(@PathVariable String customerName) {
        return reservationService.getReservationsByCustomerName(customerName);
    }

    /**
     * Retrieves all reservations with a specified status.
     *
     * @param status the reservation status (e.g., "confirmed", "cancelled")
     * @return a list of reservations matching the given status
     */
    @GetMapping("/status/{status}")
    public List<Reservation> getReservationsByStatus(@PathVariable String status) {
        return reservationService.getReservationsByStatus(status);
    }

    /**
     * Creates a new reservation using the provided details.
     *
     * @param reservation the reservation object to be created
     * @return a ResponseEntity containing the saved reservation
     */
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        Reservation saved = reservationService.createReservation(reservation);
        return ResponseEntity.ok(saved);
    }

    /**
     * Updates an existing reservation.
     *
     * @param id      the ID of the reservation to update
     * @param updated the reservation data to be updated
     * @return a ResponseEntity containing the updated reservation
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
     * Cancels a reservation by setting its status to "cancelled".
     *
     * @param id the ID of the reservation to cancel
     * @return a ResponseEntity indicating no content (204) if successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Confirms a reservation and associates it with a specific table.
     *
     * @param id      the ID of the reservation to confirm
     * @param request a map containing the tableId to assign
     * @return a ResponseEntity indicating no content (204) if successful
     */
    @PutMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmReservation(
            @PathVariable Long id,
            @RequestBody Map<String, Long> request
    ) {
        Long tableId = request.get("tableId");
        reservationService.confirmReservation(id, tableId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves all reservations scheduled for the current date.
     *
     * @return a list of today's reservations
     */
    @GetMapping("/today")
    public List<Reservation> getTodayReservations() {
        return reservationService.getTodayReservations();
    }
}
