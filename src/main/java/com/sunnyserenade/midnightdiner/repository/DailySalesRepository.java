package com.sunnyserenade.midnightdiner.repository;

import com.sunnyserenade.midnightdiner.entity.DailySales;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailySalesRepository extends JpaRepository<DailySales, Long> {
    Optional<DailySales> findByDate(LocalDate date);

    List<DailySales> findByDateBetween(LocalDate startDate, LocalDate endDate);
}