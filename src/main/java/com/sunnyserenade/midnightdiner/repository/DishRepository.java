package com.sunnyserenade.midnightdiner.repository;

import com.sunnyserenade.midnightdiner.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByCategory(String category);
    Dish findByName(String name);
}
