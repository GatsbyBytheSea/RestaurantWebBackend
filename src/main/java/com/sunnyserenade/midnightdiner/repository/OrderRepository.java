package com.sunnyserenade.midnightdiner.repository;

import com.sunnyserenade.midnightdiner.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.table.id = :tableId AND o.status = :status")
    List<Order> findByTableAndStatus(@Param("tableId") Long tableId, @Param("status") String status);

    @Query("SELECT o FROM Order o WHERE o.status = :status")
    Page<Order> findByStatus(@Param("status") String status, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.status = 'CLOSED' AND o.closeTime BETWEEN :start AND :end")
    Page<Order> findClosedOrdersByCloseTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.status = 'CLOSED' AND o.closeTime BETWEEN :start AND :end")
    List<Order> findClosedOrdersByCloseTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = 'CLOSED' AND o.closeTime BETWEEN :start AND :end")
    BigDecimal sumTotalAmountByCloseTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}