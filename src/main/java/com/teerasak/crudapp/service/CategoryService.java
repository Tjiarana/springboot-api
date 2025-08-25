package com.teerasak.crudapp.service;

import com.teerasak.crudapp.entitity.Category;
import com.teerasak.crudapp.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(int id) {
        return categoryRepository.findById(id);
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Optional<Category> updateCategory(int id, Category category) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isPresent()) {
            Category cat = existingCategory.get();
            cat.setCategoryName(category.getCategoryName());
            cat.setCategoryStatus(category.getCategoryStatus());
            return Optional.of(categoryRepository.save(cat));
        } else {
            return Optional.empty();
        }
    }

    public Optional<Category> deleteCategory(int id) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isPresent()) {
            categoryRepository.delete(existingCategory.get());
            return existingCategory;
        } else {
            return Optional.empty();
        }
    }
}
