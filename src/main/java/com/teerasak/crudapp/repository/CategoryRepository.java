package com.teerasak.crudapp.repository;

import com.teerasak.crudapp.entitity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
