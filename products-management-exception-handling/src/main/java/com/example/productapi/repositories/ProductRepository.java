package com.example.productapi.repositories;

import com.example.productapi.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findDistinctByCategories_NameAndActiveTrue(String categoryName);

    List<ProductEntity> findByNameContainingIgnoreCase(String name);

    List<ProductEntity> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
}
