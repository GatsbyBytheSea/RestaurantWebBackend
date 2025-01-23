package com.sunnyserenade.midnightdiner.controller;

import com.sunnyserenade.midnightdiner.entity.Dish;
import com.sunnyserenade.midnightdiner.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/dishes")
public class DishController {

    @Autowired
    private DishService dishService;

    // 上传图片
    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file uploaded");
        }
        try {
            String folderPath = "src/main/resources/static/images/dishes";
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String newFilename = System.currentTimeMillis() + "_" + originalFilename;

            File dest = new File(folder, newFilename);
            file.transferTo(dest);      // 将上传的文件保存到服务器本地

            // 构建可以访问的相对URL
            String imageUrl = "/images/dishes/" + newFilename;

            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("File upload failed");
        }
    }

    // 列出所有菜品
    @GetMapping
    public List<Dish> getAllDishes() {
        return dishService.getAllDishes();
    }

    // 根据id获取菜品
    @GetMapping("/{id}")
    public ResponseEntity<Dish> getDishById(@PathVariable Long id) {
        Dish dish = dishService.getDish(id);
        return ResponseEntity.ok(dish);
    }

    // 新增菜品
    @PostMapping
    public ResponseEntity<Dish> createDish(@RequestBody Dish dish) {
        Dish created = dishService.createDish(dish);
        return ResponseEntity.ok(created);
    }

    // 更新菜品信息
    @PutMapping("/{id}")
    public ResponseEntity<Dish> updateDish(@PathVariable Long id, @RequestBody Dish updated) {
        Dish result = dishService.updateDish(id, updated);
        return ResponseEntity.ok(result);
    }

    // 删除菜品
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }
}
