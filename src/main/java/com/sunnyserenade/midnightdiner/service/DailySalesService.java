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

/**
 * Service handling daily sales record creation and retrieval.
 */
@Service
public class DailySalesService {

    /**
     * Repository for accessing and saving daily sales records.
     */
    @Autowired
    private DailySalesRepository dailySalesRepository;

    /**
     * Creates or updates the daily sales record for today's date with a new amount.
     *
     * <p>If today's record does not exist, a new one is created with totalSales = 0,
     * then the given amount is added.</p>
     *
     * @param amount the amount to add to today's sales
     */
    @Transactional
    public void recordDailySales(BigDecimal amount) {
        LocalDate today = LocalDate.now();
        DailySales dailySales = dailySalesRepository.findByDate(today)
                .orElseGet(() -> {
                    // If no sales record for today, create a new one.
                    DailySales newRecord = new DailySales();
                    newRecord.setDate(today);
                    newRecord.setTotalSales(BigDecimal.ZERO);
                    newRecord.setCreateTime(LocalDateTime.now());
                    return newRecord;
                });

        dailySales.setTotalSales(dailySales.getTotalSales().add(amount));
        dailySalesRepository.save(dailySales);
    }

    /**
     * Retrieves a list of daily sales entries between the given start and end dates (inclusive).
     *
     * @param startDate the start date of the desired range
     * @param endDate   the end date of the desired range
     * @return a list of {@link DailySales} objects in the specified date range
     */
    public List<DailySales> getSalesBetween(LocalDate startDate, LocalDate endDate) {
        return dailySalesRepository.findByDateBetween(startDate, endDate);
    }
}
