package com.sunnyserenade.midnightdiner.controller;

import com.sunnyserenade.midnightdiner.entity.Reservation;
import com.sunnyserenade.midnightdiner.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for public-facing reservation endpoints.
 */
@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    /**
     * Service layer for handling reservation-related operations.
     */
    @Autowired
    private ReservationService reservationService;

    /**
     * Creates a new reservation.
     *
     * @param reservation the reservation details
     * @return a CREATED response entity containing the newly saved reservation
     */
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        Reservation saved = reservationService.createReservation(reservation);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * Retrieves an existing reservation by its ID.
     *
     * @param id the unique identifier of the reservation
     * @return the requested reservation
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    /**
     * Updates an existing reservation with new data.
     *
     * @param id      the ID of the reservation to be updated
     * @param updated the updated reservation data
     * @return the updated reservation
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
     * Cancels an existing reservation by setting its status to canceled.
     *
     * @param id the ID of the reservation to be canceled
     * @return a NO_CONTENT response on successful cancellation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }
}
