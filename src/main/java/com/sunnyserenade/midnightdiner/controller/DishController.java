package com.sunnyserenade.midnightdiner.controller;

import com.sunnyserenade.midnightdiner.entity.Dish;
import com.sunnyserenade.midnightdiner.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Controller that handles dish-related operations, including image uploads.
 */
@RestController
@RequestMapping("/api/v1/admin/dishes")
public class DishController {

    /**
     * Service for dish-related business logic.
     */
    @Autowired
    private DishService dishService;


    /**
     * The URL of the image server.
     */
    @Value("${image.server.url}")
    private String imageServerUrl;

    /**
     * Endpoint for uploading a dish image.
     *
     * @param file the multipart file containing the image to be uploaded
     * @return a ResponseEntity containing the URL of the uploaded image if successful,
     *         or a relevant error message otherwise
     */
    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        // Check for empty file
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file uploaded");
        }
        try {
            // Folder where images are stored
            String folderPath = "/var/www/images/dishes/";
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // Create a new filename to avoid collisions
            String originalFilename = file.getOriginalFilename();
            String newFilename = System.currentTimeMillis() + "_" + originalFilename;

            File dest = new File(folder, newFilename);

            // Save file to the server
            file.transferTo(dest);

            String imageUrl = imageServerUrl + newFilename;

            // Return the publicly accessible URL
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("File upload failed");
        }
    }

    /**
     * Retrieves all dishes.
     *
     * @return a list of all {@link Dish} objects
     */
    @GetMapping
    public List<Dish> getAllDishes() {
        return dishService.getAllDishes();
    }

    /**
     * Retrieves a specific dish by its ID.
     *
     * @param id the unique identifier of the dish
     * @return a ResponseEntity containing the requested dish
     */
    @GetMapping("/{id}")
    public ResponseEntity<Dish> getDishById(@PathVariable Long id) {
        Dish dish = dishService.getDish(id);
        return ResponseEntity.ok(dish);
    }

    /**
     * Creates a new dish.
     *
     * @param dish the dish object to be created
     * @return a ResponseEntity containing the newly created dish
     */
    @PostMapping
    public ResponseEntity<Dish> createDish(@RequestBody Dish dish) {
        Dish created = dishService.createDish(dish);
        return ResponseEntity.ok(created);
    }

    /**
     * Updates an existing dish.
     *
     * @param id      the ID of the dish to update
     * @param updated the dish data to update
     * @return a ResponseEntity containing the updated dish
     */
    @PutMapping("/{id}")
    public ResponseEntity<Dish> updateDish(@PathVariable Long id, @RequestBody Dish updated) {
        Dish result = dishService.updateDish(id, updated);
        return ResponseEntity.ok(result);
    }

    /**
     * Deletes a specific dish by its ID.
     *
     * @param id the unique identifier of the dish
     * @return a ResponseEntity indicating no content on success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }
}
