package com.sunnyserenade.midnightdiner.service;

import com.sunnyserenade.midnightdiner.entity.DailySales;
import com.sunnyserenade.midnightdiner.repository.DailySalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DailySalesService {
    @Autowired
    private DailySalesRepository dailySalesRepository;


    @Transactional
    public void recordDailySales(BigDecimal amount) {
        LocalDate today = LocalDate.now();
        DailySales dailySales = dailySalesRepository.findByDate(today)
                .orElseGet(() -> {
                    DailySales newRecord = new DailySales();
                    newRecord.setDate(today);
                    newRecord.setTotalSales(BigDecimal.ZERO);
                    newRecord.setCreateTime(LocalDateTime.now());
                    return newRecord;
                });

        dailySales.setTotalSales(dailySales.getTotalSales().add(amount));
        dailySalesRepository.save(dailySales);
    }

    public List<DailySales> getSalesBetween(LocalDate startDate, LocalDate endDate) {
        return dailySalesRepository.findByDateBetween(startDate, endDate);
    }

}