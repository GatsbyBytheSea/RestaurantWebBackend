package com.sunnyserenade.midnightdiner.repository;

import com.sunnyserenade.midnightdiner.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
}

