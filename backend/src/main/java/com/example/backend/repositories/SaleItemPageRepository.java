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

    Page<SaleItem> findByBrand_NameInAndPriceBetweenAndStorageGbInOrderByBrand_NameAsc(
            List<String> brands,
            Integer minPrice,
            Integer maxPrice,
            List<Integer> storageGb,
            Pageable pageable);

    Page<SaleItem> findByBrand_NameInAndPriceBetweenAndStorageGbInOrderByBrand_NameDesc(
            List<String> brands,
            Integer minPrice,
            Integer maxPrice,
            List<Integer> storageGb,
            Pageable pageable);

    Page<SaleItem> findByBrand_NameInAndPriceBetweenOrderByBrand_NameAsc(
            List<String> brands,
            Integer minPrice,
            Integer maxPrice,
            Pageable pageable
    );

    Page<SaleItem> findByBrand_NameInAndPriceBetweenOrderByBrand_NameDesc(
            List<String> brands,
            Integer minPrice,
            Integer maxPrice,
            Pageable pageable
    );
    Page<SaleItem> findByPriceBetweenOrderByBrand_NameAsc(Integer minPrice, Integer maxPrice, Pageable pageable);

    Page<SaleItem> findByPriceBetweenOrderByBrand_NameDesc(Integer minPrice, Integer maxPrice, Pageable pageable);

    Page<SaleItem> findByPriceBetweenAndStorageGbInOrderByBrand_NameAsc(
            Integer minPrice,
            Integer maxPrice,
            List<Integer> storageGb,
            Pageable pageable);

    Page<SaleItem> findByPriceBetweenAndStorageGbInOrderByBrand_NameDesc(
            Integer minPrice,
            Integer maxPrice,
            List<Integer> storageGb,
            Pageable pageable);
    Page<SaleItem> findByStorageGbInOrderByBrand_NameAsc(List<Integer> storageGb, Pageable pageable);

    Page<SaleItem> findByStorageGbInOrderByBrand_NameDesc(List<Integer> storageGb, Pageable pageable);
}
