package com.example.backend.repositories;

import com.example.backend.entities.SaleItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleItemPageRepository extends JpaRepository<SaleItem, Integer> {

    Page<SaleItem> findAllByOrderByCreatedOn(Pageable pageable);

    Page<SaleItem> findByBrand_NameInOrderByBrand_NameAsc(List<String> brands , Pageable pageable);

    Page<SaleItem> findByBrand_NameInOrderByBrand_NameDesc(List<String> brands , Pageable pageable);

    Page<SaleItem> findByBrand_NameInOrderByCreatedOn(List<String> brands, Pageable pageable);

    Page<SaleItem> findByBrand_NameInOrderByBrand_CreatedOn(List<String> brands, Pageable pageable);

    Page<SaleItem> findAllByOrderByBrandNameAsc(Pageable pageable);

    Page<SaleItem> findAllByOrderByBrandNameDesc(Pageable pageable);
}
