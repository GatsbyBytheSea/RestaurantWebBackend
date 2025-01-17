package com.sunnyserenade.midnightdiner.repository;

import com.sunnyserenade.midnightdiner.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
// 使用 Spring Data JPA，继承 JpaRepository 即可快速实现 CRUD 操作。
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByCustomerPhone(String phone);
    List<Reservation> findByCustomerName(String customerName);
    List<Reservation> findByReservationTimeBetween(LocalDateTime start, LocalDateTime end);
}