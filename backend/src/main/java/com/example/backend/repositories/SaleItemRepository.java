package com.example.backend.repositories;

import com.example.backend.entities.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SaleItemRepository extends JpaRepository<SaleItem, Integer> {
    List<SaleItem> findAllByOrderByCreatedOn();

    @Query("SELECT s FROM SaleItem s  WHERE s.brand.name IN (:filterBrands)")
    List<SaleItem> findByBrandName(List<String> filterBrands);
}
