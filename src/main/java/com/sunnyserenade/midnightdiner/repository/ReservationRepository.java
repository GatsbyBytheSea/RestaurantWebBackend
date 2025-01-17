package com.sunnyserenade.midnightdiner.repository;

import com.sunnyserenade.midnightdiner.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// 使用 Spring Data JPA，继承 JpaRepository 即可快速实现 CRUD 操作。
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    //todo: 增加额外的增删查改功能，如：
    // List<Reservation> findByCustomerPhone(String phone);     根据手机号查询订餐信息
    // List<Reservation> findByReservationTimeBetween(LocalDateTime start, LocalDateTime end);      查询特定时间区间内的订桌信息
}