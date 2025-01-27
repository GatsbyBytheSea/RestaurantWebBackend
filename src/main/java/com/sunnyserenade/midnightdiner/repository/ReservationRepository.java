package com.sunnyserenade.midnightdiner.repository;

import com.sunnyserenade.midnightdiner.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r LEFT JOIN FETCH r.table WHERE r.customerPhone = :phone")
    List<Reservation> findByCustomerPhone(@Param("phone") String phone);

    @Query("SELECT r FROM Reservation r LEFT JOIN FETCH r.table WHERE r.customerName = :customerName")
    List<Reservation> findByCustomerName(@Param("customerName") String customerName);

    @Query("SELECT r FROM Reservation r LEFT JOIN FETCH r.table WHERE r.reservationTime BETWEEN :start AND :end")
    List<Reservation> findByReservationTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT r FROM Reservation r LEFT JOIN FETCH r.table WHERE r.status = :status")
    List<Reservation> findByStatus(@Param("status") String status);
}