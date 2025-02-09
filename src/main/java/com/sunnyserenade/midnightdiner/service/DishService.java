package com.sunnyserenade.midnightdiner.service;

import com.sunnyserenade.midnightdiner.entity.Dish;
import com.sunnyserenade.midnightdiner.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DishService {

    @Autowired
    private DishRepository dishRepository;

    public Dish createDish(Dish dish) {
        dish.setCreateTime(LocalDateTime.now());
        dish.setUpdateTime(LocalDateTime.now());
        return dishRepository.save(dish);
    }

    public Dish getDish(Long id) {
        return dishRepository.findById(id).orElseThrow(() -> new RuntimeException("Dish not found"));
    }

    public Dish getDishesByName(String name) {
        return dishRepository.findByName(name);
    }

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public Dish updateDish(Long id, Dish updated) {
        Dish existing = getDish(id);
        existing.setName(updated.getName());
        existing.setCategory(updated.getCategory());
        existing.setPrice(updated.getPrice());
        existing.setDescription(updated.getDescription());
        existing.setIngredients(updated.getIngredients());
        existing.setImageUrl(updated.getImageUrl());
        existing.setUpdateTime(LocalDateTime.now());
        return dishRepository.save(existing);
    }

    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }
}
