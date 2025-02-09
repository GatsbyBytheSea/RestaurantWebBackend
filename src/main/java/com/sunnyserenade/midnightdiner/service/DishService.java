package com.sunnyserenade.midnightdiner.service;

import com.sunnyserenade.midnightdiner.entity.Dish;
import com.sunnyserenade.midnightdiner.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service handling operations related to dishes (create, read, update, delete).
 */
@Service
public class DishService {

    /**
     * Repository for dish entity interactions.
     */
    @Autowired
    private DishRepository dishRepository;

    /**
     * Creates a new dish record, initializing its createTime and updateTime.
     *
     * @param dish the dish object to be saved
     * @return the saved dish with generated ID and timestamps
     */
    public Dish createDish(Dish dish) {
        dish.setCreateTime(LocalDateTime.now());
        dish.setUpdateTime(LocalDateTime.now());
        return dishRepository.save(dish);
    }

    /**
     * Retrieves a dish by its ID.
     *
     * @param id the unique ID of the dish to retrieve
     * @return the dish if found
     * @throws RuntimeException if no dish is found with the given ID
     */
    public Dish getDish(Long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found"));
    }

    /**
     * Retrieves a dish by its name.
     *
     * @param name the name of the dish to retrieve
     * @return the matching dish, or null if not found
     */
    public Dish getDishesByName(String name) {
        return dishRepository.findByName(name);
    }

    /**
     * Retrieves all dishes.
     *
     * @return a list of all dishes in the repository
     */
    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    /**
     * Updates an existing dish with new data.
     *
     * @param id      the unique ID of the dish to update
     * @param updated the updated dish details
     * @return the updated dish entity
     */
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

    /**
     * Deletes a dish by its ID.
     *
     * @param id the unique ID of the dish to delete
     */
    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }
}
