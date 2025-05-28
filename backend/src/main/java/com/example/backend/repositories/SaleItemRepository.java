package com.example.backend.repositories;

import com.example.backend.entities.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleItemRepository extends JpaRepository<SaleItem, Integer> {
    List<SaleItem> findAllByOrderByCreatedOn();

    List<SaleItem> findByBrand_NameInOrderByBrand_NameAsc(List<String> brands);

    List<SaleItem> findByBrand_NameInOrderByBrand_NameDesc(List<String> brands);

    List<SaleItem> findByBrand_NameInOrderByCreatedOnAsc(List<String> brands);

    List<SaleItem> findAllByOrderByBrandNameAsc();

    List<SaleItem> findAllByOrderByBrandNameDesc();
}
