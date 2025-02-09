package com.sunnyserenade.midnightdiner.service;

import com.sunnyserenade.midnightdiner.entity.Reservation;
import com.sunnyserenade.midnightdiner.entity.RestaurantTable;
import com.sunnyserenade.midnightdiner.repository.ReservationRepository;
import com.sunnyserenade.midnightdiner.repository.RestaurantTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service handling reservation creation, updates, cancellations, and lookups.
 */
@Service
public class ReservationService {

    /**
     * Repository for reservation entity operations.
     */
    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * Repository for table entity operations.
     */
    @Autowired
    private RestaurantTableRepository tableRepository;

    /**
     * Creates a new reservation with default status and timestamps.
     *
     * @param reservation the reservation data to be saved
     * @return the saved reservation entity
     */
    public Reservation createReservation(Reservation reservation) {
        reservation.setStatus("CREATED");
        reservation.setCreateTime(LocalDateTime.now());
        reservation.setUpdateTime(LocalDateTime.now());
        return reservationRepository.save(reservation);
    }

    /**
     * Retrieves a reservation by its ID.
     *
     * @param id the unique ID of the reservation
     * @return the found reservation
     * @throws RuntimeException if no reservation is found with the given ID
     */
    public Reservation getReservationById(Long id) {
        Optional<Reservation> opt = reservationRepository.findById(id);
        return opt.orElseThrow(() -> new RuntimeException("Reservation not found with id = " + id));
    }

    /**
     * Retrieves reservations by the customer's phone number.
     *
     * @param phone the phone number to search by
     * @return a list of matching reservations
     */
    public List<Reservation> getReservationsByCustomerPhone(String phone) {
        return reservationRepository.findByCustomerPhone(phone);
    }

    /**
     * Retrieves reservations by the customer's name.
     *
     * @param customerName the name of the customer
     * @return a list of matching reservations
     */
    public List<Reservation> getReservationsByCustomerName(String customerName) {
        return reservationRepository.findByCustomerName(customerName);
    }

    /**
     * Retrieves reservations by their current status.
     *
     * @param status the status to filter (e.g. "CREATED", "CONFIRMED", "CANCELLED")
     * @return a list of matching reservations
     */
    public List<Reservation> getReservationsByStatus(String status) {
        return reservationRepository.findByStatus(status);
    }

    /**
     * Retrieves all reservations.
     *
     * @return a list of all reservations
     */
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    /**
     * Updates an existing reservation with new data (time, guest count, etc.).
     *
     * @param id      the unique ID of the reservation
     * @param updated the updated reservation data
     * @return the updated reservation entity
     */
    public Reservation updateReservation(Long id, Reservation updated) {
        Reservation existing = getReservationById(id);
        existing.setReservationTime(updated.getReservationTime());
        existing.setNumberOfGuests(updated.getNumberOfGuests());
        existing.setCustomerName(updated.getCustomerName());
        existing.setCustomerPhone(updated.getCustomerPhone());
        existing.setUpdateTime(LocalDateTime.now());
        return reservationRepository.save(existing);
    }

    /**
     * Confirms a reservation, linking it to a specific table and updating both records.
     *
     * @param id      the unique ID of the reservation
     * @param tableId the ID of the table to reserve
     * @throws RuntimeException if the reservation or table doesn't exist, or the table isn't available
     */
    public void confirmReservation(Long id, Long tableId) {
        Reservation existing = getReservationById(id);
        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Table not found"));

        if (!"AVAILABLE".equals(table.getStatus())) {
            throw new RuntimeException("Table is not available");
        }

        // Update reservation to confirmed
        existing.setStatus("CONFIRMED");
        existing.setTable(table);
        existing.setUpdateTime(LocalDateTime.now());

        // Mark table as reserved
        table.setStatus("RESERVED");
        table.setUpdateTime(LocalDateTime.now());
        tableRepository.save(table);

        reservationRepository.save(existing);
    }

    /**
     * Cancels a reservation, updating its status and making the table available again if reserved.
     *
     * @param id the unique ID of the reservation
     * @throws RuntimeException if the reservation doesn't exist
     */
    public void cancelReservation(Long id) {
        Reservation existing = getReservationById(id);
        existing.setStatus("CANCELLED");
        existing.setUpdateTime(LocalDateTime.now());

        // If the reservation had a table, free it up
        RestaurantTable table = existing.getTable();
        if (table != null && "RESERVED".equals(table.getStatus())) {
            table.setStatus("AVAILABLE");
            table.setUpdateTime(LocalDateTime.now());
            tableRepository.save(table);
        }

        reservationRepository.save(existing);
    }

    /**
     * Retrieves all reservations scheduled for today (00:00 to 23:59).
     *
     * @return a list of today's reservations
     */
    public List<Reservation> getTodayReservations() {
        LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        return reservationRepository.findByReservationTimeBetween(start, end);
    }
}
