package com.example.ucitelnice.repository;

import com.example.ucitelnice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
