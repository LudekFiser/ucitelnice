package com.example.ucitelnice.repository;

import com.example.ucitelnice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> getAllByActive(Boolean active);
}
