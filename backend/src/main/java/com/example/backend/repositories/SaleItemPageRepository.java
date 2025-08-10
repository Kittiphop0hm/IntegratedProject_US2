package com.example.backend.repositories;

import com.example.backend.entities.SaleItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SaleItemPageRepository extends JpaRepository<SaleItem, Integer> {

    Page<SaleItem> findAllByOrderByCreatedOn(Pageable pageable);

    Page<SaleItem> findByBrand_NameInOrderByBrand_NameAsc(List<String> brands, Pageable pageable);

    Page<SaleItem> findByBrand_NameInOrderByBrand_NameDesc(List<String> brands, Pageable pageable);

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

    Page<SaleItem> findByStorageGbInOrStorageGbIsNullOrderByBrand_NameAsc(List<Integer> storageGbs, Pageable pageable);

    Page<SaleItem> findByStorageGbInOrStorageGbIsNullOrderByBrand_NameDesc(List<Integer> storageGbs, Pageable pageable);

    // ----------- Query methods with @Query -------------

    @Query("""
        SELECT s FROM SaleItem s
        WHERE s.price BETWEEN :minPrice AND :maxPrice
          AND (s.storageGb IN :storageGbList OR s.storageGb IS NULL)
        ORDER BY s.brand.name ASC
    """)
    Page<SaleItem> findByPriceBetweenAndStorageGbInOrStorageGbIsNullOrderByBrand_NameAsc(
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("storageGbList") List<Integer> storageGbList,
            Pageable pageable);

    @Query("""
        SELECT s FROM SaleItem s
        WHERE s.price BETWEEN :minPrice AND :maxPrice
          AND (s.storageGb IN :storageGbList OR s.storageGb IS NULL)
        ORDER BY s.brand.name DESC
    """)
    Page<SaleItem> findByPriceBetweenAndStorageGbInOrStorageGbIsNullOrderByBrand_NameDesc(
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("storageGbList") List<Integer> storageGbList,
            Pageable pageable);

    @Query("""
        SELECT s FROM SaleItem s
        WHERE s.brand.name IN :brands
          AND s.price BETWEEN :minPrice AND :maxPrice
          AND (s.storageGb IN :storageGbList OR s.storageGb IS NULL)
        ORDER BY s.brand.name ASC
    """)
    Page<SaleItem> findByBrand_NameInAndPriceBetweenAndStorageGbInOrStorageGbIsNullOrderByBrand_NameAsc(
            @Param("brands") List<String> brands,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("storageGbList") List<Integer> storageGbList,
            Pageable pageable);

    @Query("""
        SELECT s FROM SaleItem s
        WHERE s.brand.name IN :brands
          AND s.price BETWEEN :minPrice AND :maxPrice
          AND (s.storageGb IN :storageGbList OR s.storageGb IS NULL)
        ORDER BY s.brand.name DESC
    """)
    Page<SaleItem> findByBrand_NameInAndPriceBetweenAndStorageGbInOrStorageGbIsNullOrderByBrand_NameDesc(
            @Param("brands") List<String> brands,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("storageGbList") List<Integer> storageGbList,
            Pageable pageable);

    Page<SaleItem> findByStorageGbIsNullOrderByBrand_NameAsc(Pageable pageable);

    Page<SaleItem> findByStorageGbIsNullOrderByBrand_NameDesc(Pageable pageable);
    Page<SaleItem> findByPriceBetweenAndStorageGbIsNullOrderByBrand_NameAsc(
            Integer minPrice, Integer maxPrice, Pageable pageable);

    Page<SaleItem> findByPriceBetweenAndStorageGbIsNullOrderByBrand_NameDesc(
            Integer minPrice, Integer maxPrice, Pageable pageable);

}
