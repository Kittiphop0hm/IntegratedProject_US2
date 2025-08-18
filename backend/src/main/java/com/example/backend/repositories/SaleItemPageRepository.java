package com.example.backend.repositories;

import com.example.backend.entities.SaleItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SaleItemPageRepository extends JpaRepository<SaleItem, Integer> {

    // ========== BASIC SORTING METHODS ==========
    Page<SaleItem> findAllByOrderByCreatedOn(Pageable pageable);
    Page<SaleItem> findAllByOrderByCreatedOnAsc(Pageable pageable);
    Page<SaleItem> findAllByOrderByCreatedOnDesc(Pageable pageable);  // เพิ่มใหม่
    Page<SaleItem> findAllByOrderByBrandNameAsc(Pageable pageable);
    Page<SaleItem> findAllByOrderByBrandNameDesc(Pageable pageable);

    // ========== BRAND FILTERING WITH SORTING ==========
    Page<SaleItem> findByBrand_NameInOrderByBrand_NameAsc(List<String> brands, Pageable pageable);
    Page<SaleItem> findByBrand_NameInOrderByBrand_NameDesc(List<String> brands, Pageable pageable);
    Page<SaleItem> findByBrand_NameInOrderByCreatedOn(List<String> brands, Pageable pageable);
    Page<SaleItem> findByBrand_NameInOrderByCreatedOnAsc(List<String> brands, Pageable pageable);
    Page<SaleItem> findByBrand_NameInOrderByCreatedOnDesc(List<String> brands, Pageable pageable);  // เพิ่มใหม่
    Page<SaleItem> findByBrand_NameInOrderByBrand_CreatedOn(List<String> brands, Pageable pageable);

    // ========== PRICE FILTERING ==========
    Page<SaleItem> findByPriceBetweenOrderByCreatedOn(Integer minPrice, Integer maxPrice, Pageable pageable);
    Page<SaleItem> findByPriceBetweenOrderByCreatedOnAsc(Integer minPrice, Integer maxPrice, Pageable pageable);
    Page<SaleItem> findByPriceBetweenOrderByCreatedOnDesc(Integer minPrice, Integer maxPrice, Pageable pageable);  // เพิ่มใหม่
    Page<SaleItem> findByPriceBetweenOrderByBrand_NameAsc(Integer minPrice, Integer maxPrice, Pageable pageable);
    Page<SaleItem> findByPriceBetweenOrderByBrand_NameDesc(Integer minPrice, Integer maxPrice, Pageable pageable);

    // ========== BRAND + PRICE FILTERING ==========
    Page<SaleItem> findByBrand_NameInAndPriceBetweenOrderByCreatedOn(List<String> brands, Integer minPrice, Integer maxPrice, Pageable pageable);
    Page<SaleItem> findByBrand_NameInAndPriceBetweenOrderByCreatedOnAsc(List<String> brands, Integer minPrice, Integer maxPrice, Pageable pageable);
    Page<SaleItem> findByBrand_NameInAndPriceBetweenOrderByCreatedOnDesc(List<String> brands, Integer minPrice, Integer maxPrice, Pageable pageable);  // เพิ่มใหม่
    Page<SaleItem> findByBrand_NameInAndPriceBetweenOrderByBrand_NameAsc(
            List<String> brands,
            Integer minPrice,
            Integer maxPrice,
            Pageable pageable);
    Page<SaleItem> findByBrand_NameInAndPriceBetweenOrderByBrand_NameDesc(
            List<String> brands,
            Integer minPrice,
            Integer maxPrice,
            Pageable pageable);

    // ========== STORAGE FILTERING ==========
    Page<SaleItem> findByStorageGbInOrderByCreatedOn(List<Integer> storageGb, Pageable pageable);
    Page<SaleItem> findByStorageGbInOrderByCreatedOnAsc(List<Integer> storageGb, Pageable pageable);
    Page<SaleItem> findByStorageGbInOrderByCreatedOnDesc(List<Integer> storageGb, Pageable pageable);  // เพิ่มใหม่
    Page<SaleItem> findByStorageGbInOrderByBrand_NameAsc(List<Integer> storageGb, Pageable pageable);
    Page<SaleItem> findByStorageGbInOrderByBrand_NameDesc(List<Integer> storageGb, Pageable pageable);

    // ========== PRICE + STORAGE FILTERING ==========
    Page<SaleItem> findByPriceBetweenAndStorageGbInOrderByCreatedOn(Integer minPrice, Integer maxPrice, List<Integer> storageGb, Pageable pageable);
    Page<SaleItem> findByPriceBetweenAndStorageGbInOrderByCreatedOnAsc(Integer minPrice, Integer maxPrice, List<Integer> storageGb, Pageable pageable);
    Page<SaleItem> findByPriceBetweenAndStorageGbInOrderByCreatedOnDesc(Integer minPrice, Integer maxPrice, List<Integer> storageGb, Pageable pageable);  // เพิ่มใหม่
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

    // ========== BRAND + STORAGE FILTERING ==========
    Page<SaleItem> findByBrand_NameInAndStorageGbInOrderByCreatedOn(List<String> brandNames, List<Integer> storageSizes, Pageable pageable);
    Page<SaleItem> findByBrand_NameInAndStorageGbInOrderByCreatedOnAsc(List<String> brandNames, List<Integer> storageSizes, Pageable pageable);
    Page<SaleItem> findByBrand_NameInAndStorageGbInOrderByCreatedOnDesc(List<String> brandNames, List<Integer> storageSizes, Pageable pageable);  // เพิ่มใหม่
    Page<SaleItem> findByBrand_NameInAndStorageGbInOrderByBrand_NameAsc(
            List<String> brandNames, List<Integer> storageSizes, Pageable pageable);
    Page<SaleItem> findByBrand_NameInAndStorageGbInOrderByBrand_NameDesc(
            List<String> brandNames, List<Integer> storageSizes, Pageable pageable);

    // ========== STORAGE EQUALS 0 (NOT SPECIFIED) ==========
    Page<SaleItem> findByStorageGbEqualsOrderByCreatedOn(Integer storageGb, Pageable pageable);
    Page<SaleItem> findByStorageGbEqualsOrderByCreatedOnAsc(Integer storageGb, Pageable pageable);
    Page<SaleItem> findByStorageGbEqualsOrderByCreatedOnDesc(Integer storageGb, Pageable pageable);  // เพิ่มใหม่
    Page<SaleItem> findByStorageGbEqualsOrderByBrand_NameAsc(Integer storageGb, Pageable pageable);
    Page<SaleItem> findByStorageGbEqualsOrderByBrand_NameDesc(Integer storageGb, Pageable pageable);

    // ========== PRICE + STORAGE EQUALS 0 ==========
    Page<SaleItem> findByPriceBetweenAndStorageGbEqualsOrderByCreatedOn(Integer minPrice, Integer maxPrice, Integer storageGb, Pageable pageable);
    Page<SaleItem> findByPriceBetweenAndStorageGbEqualsOrderByCreatedOnAsc(Integer minPrice, Integer maxPrice, Integer storageGb, Pageable pageable);
    Page<SaleItem> findByPriceBetweenAndStorageGbEqualsOrderByCreatedOnDesc(Integer minPrice, Integer maxPrice, Integer storageGb, Pageable pageable);  // เพิ่มใหม่
    Page<SaleItem> findByPriceBetweenAndStorageGbEqualsOrderByBrand_NameAsc(
            Integer minPrice, Integer maxPrice, Integer storageGb, Pageable pageable);
    Page<SaleItem> findByPriceBetweenAndStorageGbEqualsOrderByBrand_NameDesc(
            Integer minPrice, Integer maxPrice, Integer storageGb, Pageable pageable);

    // ========== BRAND + STORAGE EQUALS 0 ==========
    Page<SaleItem> findByBrand_NameInAndStorageGbEqualsOrderByCreatedOn(List<String> brandNames, Integer storageGb, Pageable pageable);
    Page<SaleItem> findByBrand_NameInAndStorageGbEqualsOrderByCreatedOnAsc(List<String> brandNames, Integer storageGb, Pageable pageable);
    Page<SaleItem> findByBrand_NameInAndStorageGbEqualsOrderByCreatedOnDesc(List<String> brandNames, Integer storageGb, Pageable pageable);  // เพิ่มใหม่
    Page<SaleItem> findByBrand_NameInAndStorageGbEqualsOrderByBrand_NameAsc(
            List<String> brandNames, Integer storageGb, Pageable pageable);
    Page<SaleItem> findByBrand_NameInAndStorageGbEqualsOrderByBrand_NameDesc(
            List<String> brandNames, Integer storageGb, Pageable pageable);

    // ========== BRAND + PRICE + STORAGE EQUALS 0 ==========
    Page<SaleItem> findByBrand_NameInAndPriceBetweenAndStorageGbEqualsOrderByCreatedOn(List<String> brandNames, Integer minPrice, Integer maxPrice, Integer storageGb, Pageable pageable);
    Page<SaleItem> findByBrand_NameInAndPriceBetweenAndStorageGbEqualsOrderByCreatedOnAsc(List<String> brandNames, Integer minPrice, Integer maxPrice, Integer storageGb, Pageable pageable);
    Page<SaleItem> findByBrand_NameInAndPriceBetweenAndStorageGbEqualsOrderByCreatedOnDesc(List<String> brandNames, Integer minPrice, Integer maxPrice, Integer storageGb, Pageable pageable);  // เพิ่มใหม่
    Page<SaleItem> findByBrand_NameInAndPriceBetweenAndStorageGbEqualsOrderByBrand_NameAsc(
            List<String> brandNames, Integer minPrice, Integer maxPrice, Integer storageGb, Pageable pageable);
    Page<SaleItem> findByBrand_NameInAndPriceBetweenAndStorageGbEqualsOrderByBrand_NameDesc(
            List<String> brandNames, Integer minPrice, Integer maxPrice, Integer storageGb, Pageable pageable);

    // ========== BRAND + PRICE + STORAGE (รองรับโค้ดใหม่) ==========
    Page<SaleItem> findByBrand_NameInAndPriceBetweenAndStorageGbInOrderByCreatedOnAsc(
            List<String> brands, Integer minPrice, Integer maxPrice, List<Integer> storageGb, Pageable pageable);

    Page<SaleItem> findByBrand_NameInAndPriceBetweenAndStorageGbInOrderByCreatedOnDesc(  // เพิ่มใหม่
                                                                                         List<String> brands, Integer minPrice, Integer maxPrice, List<Integer> storageGb, Pageable pageable);

    Page<SaleItem> findByBrand_NameInAndPriceBetweenAndStorageGbInOrderByBrand_NameAsc(
            List<String> brands, Integer minPrice, Integer maxPrice, List<Integer> storageGb, Pageable pageable);

    Page<SaleItem> findByBrand_NameInAndPriceBetweenAndStorageGbInOrderByBrand_NameDesc(
            List<String> brands, Integer minPrice, Integer maxPrice, List<Integer> storageGb, Pageable pageable);

    // ========== OR CONDITIONS WITH @Query (STORAGE IN LIST OR EQUALS 0) ==========

    // storage list OR storageGb = 0 (with createdOn sorting)
    @Query("""
        SELECT s FROM SaleItem s
        WHERE s.storageGb IN :storageSizes OR s.storageGb = :notSpecifiedValue
        ORDER BY s.createdOn ASC
    """)
    Page<SaleItem> findByStorageGbInOrStorageGbEqualsOrderByCreatedOn(
            @Param("storageSizes") List<Integer> storageSizes,
            @Param("notSpecifiedValue") Integer notSpecifiedValue,
            Pageable pageable);

    @Query("""
        SELECT s FROM SaleItem s
        WHERE s.storageGb IN :storageSizes OR s.storageGb = :notSpecifiedValue
        ORDER BY s.createdOn ASC
    """)
    Page<SaleItem> findByStorageGbInOrStorageGbEqualsOrderByCreatedOnAsc(
            @Param("storageSizes") List<Integer> storageSizes,
            @Param("notSpecifiedValue") Integer notSpecifiedValue,
            Pageable pageable);

    // เพิ่ม DESC version
    @Query("""
        SELECT s FROM SaleItem s
        WHERE s.storageGb IN :storageSizes OR s.storageGb = :notSpecifiedValue
        ORDER BY s.createdOn DESC
    """)
    Page<SaleItem> findByStorageGbInOrStorageGbEqualsOrderByCreatedOnDesc(
            @Param("storageSizes") List<Integer> storageSizes,
            @Param("notSpecifiedValue") Integer notSpecifiedValue,
            Pageable pageable);

    // price + storage list OR storageGb = 0 (with createdOn sorting)
    @Query("""
        SELECT s FROM SaleItem s
        WHERE s.price BETWEEN :minPrice AND :maxPrice
          AND (s.storageGb IN :storageGbList OR s.storageGb = :notSpecifiedValue)
        ORDER BY s.createdOn ASC
    """)
    Page<SaleItem> findByPriceBetweenAndStorageGbInOrStorageGbEqualsOrderByCreatedOn(
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("storageGbList") List<Integer> storageGbList,
            @Param("notSpecifiedValue") Integer notSpecifiedValue,
            Pageable pageable);

    @Query("""
        SELECT s FROM SaleItem s
        WHERE s.price BETWEEN :minPrice AND :maxPrice
          AND (s.storageGb IN :storageGbList OR s.storageGb = :notSpecifiedValue)
        ORDER BY s.createdOn ASC
    """)
    Page<SaleItem> findByPriceBetweenAndStorageGbInOrStorageGbEqualsOrderByCreatedOnAsc(
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("storageGbList") List<Integer> storageGbList,
            @Param("notSpecifiedValue") Integer notSpecifiedValue,
            Pageable pageable);

    // เพิ่ม DESC version
    @Query("""
        SELECT s FROM SaleItem s
        WHERE s.price BETWEEN :minPrice AND :maxPrice
          AND (s.storageGb IN :storageGbList OR s.storageGb = :notSpecifiedValue)
        ORDER BY s.createdOn DESC
    """)
    Page<SaleItem> findByPriceBetweenAndStorageGbInOrStorageGbEqualsOrderByCreatedOnDesc(
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("storageGbList") List<Integer> storageGbList,
            @Param("notSpecifiedValue") Integer notSpecifiedValue,
            Pageable pageable);

    // brand + storage list OR storageGb = 0 (with createdOn sorting)
    @Query("""
        SELECT s FROM SaleItem s
        WHERE s.brand.name IN :brandNames
          AND (s.storageGb IN :storageSizes OR s.storageGb = :notSpecifiedValue)
        ORDER BY s.createdOn ASC
    """)
    Page<SaleItem> findByBrand_NameInAndStorageGbInOrStorageGbEqualsOrderByCreatedOn(
            @Param("brandNames") List<String> brandNames,
            @Param("storageSizes") List<Integer> storageSizes,
            @Param("notSpecifiedValue") Integer notSpecifiedValue,
            Pageable pageable);

    @Query("""
        SELECT s FROM SaleItem s
        WHERE s.brand.name IN :brandNames
          AND (s.storageGb IN :storageSizes OR s.storageGb = :notSpecifiedValue)
        ORDER BY s.createdOn ASC
    """)
    Page<SaleItem> findByBrand_NameInAndStorageGbInOrStorageGbEqualsOrderByCreatedOnAsc(
            @Param("brandNames") List<String> brandNames,
            @Param("storageSizes") List<Integer> storageSizes,
            @Param("notSpecifiedValue") Integer notSpecifiedValue,
            Pageable pageable);

    // เพิ่ม DESC version
    @Query("""
        SELECT s FROM SaleItem s
        WHERE s.brand.name IN :brandNames
          AND (s.storageGb IN :storageSizes OR s.storageGb = :notSpecifiedValue)
        ORDER BY s.createdOn DESC
    """)
    Page<SaleItem> findByBrand_NameInAndStorageGbInOrStorageGbEqualsOrderByCreatedOnDesc(
            @Param("brandNames") List<String> brandNames,
            @Param("storageSizes") List<Integer> storageSizes,
            @Param("notSpecifiedValue") Integer notSpecifiedValue,
            Pageable pageable);

    // brand + price + storage list OR storageGb = 0 (with createdOn sorting)
    @Query("""
        SELECT s FROM SaleItem s
        WHERE s.brand.name IN :brands
          AND s.price BETWEEN :minPrice AND :maxPrice
          AND (s.storageGb IN :storageGbList OR s.storageGb = :notSpecifiedValue)
        ORDER BY s.createdOn ASC
    """)
    Page<SaleItem> findByBrand_NameInAndPriceBetweenAndStorageGbInOrStorageGbEqualsOrderByCreatedOn(
            @Param("brands") List<String> brands,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("storageGbList") List<Integer> storageGbList,
            @Param("notSpecifiedValue") Integer notSpecifiedValue,
            Pageable pageable);

    @Query("""
        SELECT s FROM SaleItem s
        WHERE s.brand.name IN :brands
          AND s.price BETWEEN :minPrice AND :maxPrice
          AND (s.storageGb IN :storageGbList OR s.storageGb = :notSpecifiedValue)
        ORDER BY s.createdOn ASC
    """)
    Page<SaleItem> findByBrand_NameInAndPriceBetweenAndStorageGbInOrStorageGbEqualsOrderByCreatedOnAsc(
            @Param("brands") List<String> brands,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("storageGbList") List<Integer> storageGbList,
            @Param("notSpecifiedValue") Integer notSpecifiedValue,
            Pageable pageable);

    // เพิ่ม DESC version
    @Query("""
        SELECT s FROM SaleItem s
        WHERE s.brand.name IN :brands
          AND s.price BETWEEN :minPrice AND :maxPrice
          AND (s.storageGb IN :storageGbList OR s.storageGb = :notSpecifiedValue)
        ORDER BY s.createdOn DESC
    """)
    Page<SaleItem> findByBrand_NameInAndPriceBetweenAndStorageGbInOrStorageGbEqualsOrderByCreatedOnDesc(
            @Param("brands") List<String> brands,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("storageGbList") List<Integer> storageGbList,
            @Param("notSpecifiedValue") Integer notSpecifiedValue,
            Pageable pageable);

    // ========== OR CONDITIONS WITH BRAND SORTING ==========

    // storage list OR storageGb = 0
    @Query("""
        SELECT s FROM SaleItem s
        WHERE s.storageGb IN :storageSizes OR s.storageGb = :notSpecifiedValue
        ORDER BY s.brand.name ASC
    """)
    Page<SaleItem> findByStorageGbInOrStorageGbEqualsOrderByBrand_NameAsc(
            @Param("storageSizes") List<Integer> storageSizes,
            @Param("notSpecifiedValue") Integer notSpecifiedValue,
            Pageable pageable);

    @Query("""
        SELECT s FROM SaleItem s
        WHERE s.storageGb IN :storageSizes OR s.storageGb = :notSpecifiedValue
        ORDER BY s.brand.name DESC
    """)
    Page<SaleItem> findByStorageGbInOrStorageGbEqualsOrderByBrand_NameDesc(
            @Param("storageSizes") List<Integer> storageSizes,
            @Param("notSpecifiedValue") Integer notSpecifiedValue,
            Pageable pageable);

    // price + storage list OR storageGb = 0
    @Query("""
        SELECT s FROM SaleItem s
        WHERE s.price BETWEEN :minPrice AND :maxPrice
          AND (s.storageGb IN :storageGbList OR s.storageGb = :notSpecifiedValue)
        ORDER BY s.brand.name ASC
    """)
    Page<SaleItem> findByPriceBetweenAndStorageGbInOrStorageGbEqualsOrderByBrand_NameAsc(
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("storageGbList") List<Integer> storageGbList,
            @Param("notSpecifiedValue") Integer notSpecifiedValue,
            Pageable pageable);

    @Query("""
        SELECT s FROM SaleItem s
        WHERE s.price BETWEEN :minPrice AND :maxPrice
          AND (s.storageGb IN :storageGbList OR s.storageGb = :notSpecifiedValue)
        ORDER BY s.brand.name DESC
    """)
    Page<SaleItem> findByPriceBetweenAndStorageGbInOrStorageGbEqualsOrderByBrand_NameDesc(
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("storageGbList") List<Integer> storageGbList,
            @Param("notSpecifiedValue") Integer notSpecifiedValue,
            Pageable pageable);

    // brand + storage list OR storageGb = 0
    @Query("""
        SELECT s FROM SaleItem s
        WHERE s.brand.name IN :brandNames
          AND (s.storageGb IN :storageSizes OR s.storageGb = :notSpecifiedValue)
        ORDER BY s.brand.name ASC
    """)
    Page<SaleItem> findByBrand_NameInAndStorageGbInOrStorageGbEqualsOrderByBrand_NameAsc(
            @Param("brandNames") List<String> brandNames,
            @Param("storageSizes") List<Integer> storageSizes,
            @Param("notSpecifiedValue") Integer notSpecifiedValue,
            Pageable pageable);

    @Query("""
        SELECT s FROM SaleItem s
        WHERE s.brand.name IN :brandNames
          AND (s.storageGb IN :storageSizes OR s.storageGb = :notSpecifiedValue)
        ORDER BY s.brand.name DESC
    """)
    Page<SaleItem> findByBrand_NameInAndStorageGbInOrStorageGbEqualsOrderByBrand_NameDesc(
            @Param("brandNames") List<String> brandNames,
            @Param("storageSizes") List<Integer> storageSizes,
            @Param("notSpecifiedValue") Integer notSpecifiedValue,
            Pageable pageable);

    // brand + price + storage list OR storageGb = 0 (กรณีครบทุกอย่าง)
    @Query("""
        SELECT s FROM SaleItem s
        WHERE s.brand.name IN :brands
          AND s.price BETWEEN :minPrice AND :maxPrice
          AND (s.storageGb IN :storageGbList OR s.storageGb = :notSpecifiedValue)
        ORDER BY s.brand.name ASC
    """)
    Page<SaleItem> findByBrand_NameInAndPriceBetweenAndStorageGbInOrStorageGbEqualsOrderByBrand_NameAsc(
            @Param("brands") List<String> brands,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("storageGbList") List<Integer> storageGbList,
            @Param("notSpecifiedValue") Integer notSpecifiedValue,
            Pageable pageable);

    @Query("""
        SELECT s FROM SaleItem s
        WHERE s.brand.name IN :brands
          AND s.price BETWEEN :minPrice AND :maxPrice
          AND (s.storageGb IN :storageGbList OR s.storageGb = :notSpecifiedValue)
        ORDER BY s.brand.name DESC
    """)
    Page<SaleItem> findByBrand_NameInAndPriceBetweenAndStorageGbInOrStorageGbEqualsOrderByBrand_NameDesc(
            @Param("brands") List<String> brands,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("storageGbList") List<Integer> storageGbList,
            @Param("notSpecifiedValue") Integer notSpecifiedValue,
            Pageable pageable);

    // ========== LEGACY METHODS (เก็บไว้เผื่อยังใช้ null อยู่) ==========

    Page<SaleItem> findByStorageGbInOrStorageGbIsNullOrderByBrand_NameAsc(List<Integer> storageGbs, Pageable pageable);
    Page<SaleItem> findByStorageGbInOrStorageGbIsNullOrderByBrand_NameDesc(List<Integer> storageGbs, Pageable pageable);

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