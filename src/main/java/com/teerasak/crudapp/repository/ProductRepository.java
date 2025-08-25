package com.teerasak.crudapp.repository;

import com.teerasak.crudapp.entitity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
