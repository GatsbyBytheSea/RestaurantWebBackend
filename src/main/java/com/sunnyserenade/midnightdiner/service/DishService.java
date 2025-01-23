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

    // 新增菜品
    public Dish createDish(Dish dish) {
        dish.setCreateTime(LocalDateTime.now());
        dish.setUpdateTime(LocalDateTime.now());
        return dishRepository.save(dish);
    }

    // 根据ID获取菜品
    public Dish getDish(Long id) {
        return dishRepository.findById(id).orElseThrow(() -> new RuntimeException("Dish not found"));
    }

    // 根据菜品名称查找
    public Dish getDishesByName(String name) {
        return dishRepository.findByName(name);
    }

    // 获取全部菜品
    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    // 更新菜品
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

    // 删除菜品
    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }
}
