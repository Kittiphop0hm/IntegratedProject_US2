package com.example.backend.specifications;

import com.example.backend.entities.SaleItem;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Specification คือการเขียน query แบบ type-safe โดยใช้ Criteria API
 * แทนที่จะเขียน SQL หรือ JPQL เราสามารถเขียน Java code ได้เลย
 *
 * รูปแบบคือ: (root, query, criteriaBuilder) -> { return predicate; }
 * - root = ตัวแทน entity หลัก (SaleItem)
 * - query = query builder
 * - criteriaBuilder = เครื่องมือสร้าง condition ต่างๆ
 */
public class SaleItemSpecification {

    /**
     * กรองตาม brand name
     * SQL ที่ได้: SELECT * FROM sale_item si JOIN brand b ON si.brand_id = b.id WHERE b.name IN (...)
     */
    public static Specification<SaleItem> withSearchKeyword(String searchKeyword) {
        return (root, query, cb) -> {
            // ถ้าไม่ได้ค้นหาอะไร ก็ไม่กรองอะไร
            if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
                return cb.conjunction();
            }

            // แยกคำด้วย space เช่น "flag black" → ["flag", "black"]
            String[] words = searchKeyword.toLowerCase().split("\\s+");

            // เตรียม field ที่จะค้นหา (description, model, color)
            Expression<String> description = cb.lower(root.get("description").as(String.class));
            Expression<String> model = cb.lower(root.get("model"));
            Expression<String> color = cb.lower(root.get("color"));

            // สำหรับแต่ละคำ สร้างเงื่อนไข
            List<Predicate> allWordConditions = new ArrayList<>();

            for (String word : words) {
                word = word.trim();
                if (word.isEmpty()) continue; // ข้ามคำว่าง

                String pattern = "%" + word + "%";

                // คำนี้ต้องพบในอย่างน้อย 1 field (description หรือ model หรือ color)
                Predicate wordFound = cb.or(
                        cb.like(description, pattern),
                        cb.like(model, pattern),
                        cb.like(color, pattern)
                );

                allWordConditions.add(wordFound);
            }

            // ถ้าไม่มีคำที่ค้นหาได้
            if (allWordConditions.isEmpty()) {
                return cb.conjunction();
            }

            // ทุกคำต้องพบ (AND กันทุกคำ)
            return cb.and(allWordConditions.toArray(new Predicate[0]));
        };
    }
    public static Specification<SaleItem> withBrands(List<String> brands) {
        return (root, query, cb) -> {
            // ถ้าไม่มี brand ให้กรอง ก็ไม่กรองอะไร
            if (brands == null || brands.isEmpty()) {
                return cb.conjunction(); // เหมือน WHERE 1=1 (ไม่มีเงื่อนไข)
            }

            // ต้อง JOIN กับ brand table เพื่อเอา brand name
            Join<Object, Object> brandJoin = root.join("brand", JoinType.INNER);
            return brandJoin.get("name").in(brands); // WHERE brand.name IN ('Apple', 'Samsung')
        };
    }

    /**
     * กรองตามช่วงราคา
     * SQL ที่ได้: SELECT * FROM sale_item WHERE price >= minPrice AND price <= maxPrice
     */
    public static Specification<SaleItem> withPriceRange(Integer minPrice, Integer maxPrice) {
        return (root, query, cb) -> {
            List<Predicate> conditions = new ArrayList<>();

            // ถ้ามี minPrice ให้เพิ่มเงื่อนไข price >= minPrice
            if (minPrice != null) {
                conditions.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            // ถ้ามี maxPrice ให้เพิ่มเงื่อนไข price <= maxPrice
            if (maxPrice != null) {
                conditions.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            // ถ้าไม่มีเงื่อนไขเลย return ไม่กรองอะไร
            if (conditions.isEmpty()) {
                return cb.conjunction();
            }

            // รวมเงื่อนไขด้วย AND
            return cb.and(conditions.toArray(new Predicate[0]));
        };
    }

    /**
     * กรองตาม storage ที่ระบุ (หลายค่า)
     * SQL ที่ได้: SELECT * FROM sale_item WHERE storage_gb IN (64, 128, 256)
     */
    public static Specification<SaleItem> withStorageGbIn(List<Integer> storageSizes) {
        return (root, query, cb) -> {
            if (storageSizes == null || storageSizes.isEmpty()) {
                return cb.conjunction();
            }
            return root.get("storageGb").in(storageSizes);
        };
    }



    /**
     * กรองตาม storage ที่เท่ากับค่าหนึ่ง หรือ เป็น null
     * SQL ที่ได้: SELECT * FROM sale_item WHERE storage_gb = 0 OR storage_gb IS NULL
     */
    public static Specification<SaleItem> withStorageGbEqualsOrNull(Integer storageValue) {
        return (root, query, cb) -> {
            if (storageValue == null) {
                return cb.isNull(root.get("storageGb")); // WHERE storage_gb IS NULL
            }

            return cb.or(
                    cb.equal(root.get("storageGb"), storageValue),  // WHERE storage_gb = value
                    cb.isNull(root.get("storageGb"))                // OR storage_gb IS NULL
            );
        };
    }

    /**
     * กรองตาม storage แบบซับซ้อน: IN หลายค่า หรือ เท่ากับค่าหนึ่ง หรือ null
     * SQL ที่ได้: SELECT * FROM sale_item WHERE storage_gb IN (...) OR storage_gb = 0 OR storage_gb IS NULL
     */
    public static Specification<SaleItem> withStorageGbInOrEqualsOrNull(List<Integer> storageSizes, Integer notSpecifiedValue) {
        return (root, query, cb) -> {
            List<Predicate> conditions = new ArrayList<>();

            // เงื่อนไข 1: storage_gb IN (64, 128, 256)
            if (storageSizes != null && !storageSizes.isEmpty()) {
                conditions.add(root.get("storageGb").in(storageSizes));
            }

            // เงื่อนไข 2: storage_gb = 0 OR storage_gb IS NULL
            if (notSpecifiedValue != null) {
                conditions.add(cb.or(
                        cb.equal(root.get("storageGb"), notSpecifiedValue),
                        cb.isNull(root.get("storageGb"))
                ));
            }

            // ถ้าไม่มีเงื่อนไข
            if (conditions.isEmpty()) {
                return cb.conjunction();
            }

            // ถ้ามีเงื่อนไขเดียว
            if (conditions.size() == 1) {
                return conditions.get(0);
            }

            // รวมเงื่อนไขด้วย OR
            return cb.or(conditions.toArray(new Predicate[0]));
        };
    }

    /**
     * เพิ่มการเรียงลำดับ (sorting)
     * ต้องระวัง: ห้ามใส่ ORDER BY ใน count query (เวลาทำ pagination)
     */
    public static Specification<SaleItem> withSorting(String sortField, String sortDirection) {
        return (root, query, cb) -> {
            // เช็คว่าเป็น count query หรือเปล่า (สำหรับ pagination)
            // count query จะมี return type เป็น Long
            if (query.getResultType() == Long.class || query.getResultType() == long.class) {
                return cb.conjunction(); // ไม่ทำอะไร
            }

            // กำหนด direction (ASC หรือ DESC)
            boolean isAscending = true;
            if (sortDirection != null && "desc".equals(sortDirection.trim().toLowerCase())) {
                isAscending = false;
            }

            // กำหนด field ที่จะ sort
            String field = "";
            if (sortField != null) {
                field = sortField.trim().toLowerCase();
            }

            // เลือก field ที่จะ sort และสร้าง Order
            Path<?> orderPath;

            if (field.equals("brand") || field.equals("brand.name") || field.equals("brand_name")) {
                // sort ตาม brand name ต้อง JOIN
                Join<Object, Object> brandJoin = root.join("brand", JoinType.LEFT);
                orderPath = brandJoin.get("name");

            } else if (field.equals("price")) {
                orderPath = root.get("price");

            } else if (field.equals("model")) {
                orderPath = root.get("model");

            } else if (field.equals("color")) {
                orderPath = root.get("color");

            } else if (field.equals("storage") || field.equals("storagegb")) {
                orderPath = root.get("storageGb");

            } else if (field.equals("quantity")) {
                orderPath = root.get("quantity");

            } else if (field.contains("created")) {
                // createdon, created_on, createdat, created_at เป็นต้น
                orderPath = root.get("createdOn");

            } else {
                // default: sort ตาม createdOn DESC (ใหม่สุดก่อน)
                orderPath = root.get("createdOn");
                isAscending = false;
            }

            // สร้าง Order และใส่ใน query
            Order order = isAscending ? cb.asc(orderPath) : cb.desc(orderPath);
            query.orderBy(order);

            return cb.conjunction(); // ไม่มี WHERE condition
        };
    }

    /**
     * รวมทุก filter เข้าด้วยกัน
     * วิธีใช้: repository.findAll(buildFilterSpecification(...))
     */
    /**
     * รวมทุก filter เข้าด้วยกัน (เพิ่ม searchKeyword parameter)
     * วิธีใช้: repository.findAll(buildFilterSpecification(...))
     */
    public static Specification<SaleItem> buildFilterSpecification(
            String searchKeyword,           // เพิ่มใหม่ - คำค้นหา
            List<String> filterBrands,      // brand ที่จะกรอง
            Integer minPrice,               // ราคาต่ำสุด
            Integer maxPrice,               // ราคาสูงสุด
            List<Integer> filterStorageSizes, // storage ที่จะกรอง
            boolean includeNotSpecified,    // รวม storage ที่ไม่ระบุด้วยไหม
            String sortField,               // field ที่จะ sort
            String sortDirection) {         // ทิศทาง sort (asc/desc)

        // เริ่มต้นด้วย Specification ว่าง
        Specification<SaleItem> spec = Specification.where(null);

        // เพิ่ม search keyword filter (เพิ่มใหม่)
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            spec = spec.and(withSearchKeyword(searchKeyword));
        }

        // เพิ่ม brand filter (ถ้ามี)
        if (filterBrands != null && !filterBrands.isEmpty()) {
            spec = spec.and(withBrands(filterBrands));
        }

        // เพิ่ม price filter (ถ้ามี)
        if (minPrice != null || maxPrice != null) {
            spec = spec.and(withPriceRange(minPrice, maxPrice));
        }

        // เพิ่ม storage filter (logic ซับซ้อน)
        if (includeNotSpecified && (filterStorageSizes != null && !filterStorageSizes.isEmpty())) {
            // กรองตาม storage ที่ระบุ + รวมที่ไม่ระบุด้วย
            spec = spec.and(withStorageGbInOrEqualsOrNull(filterStorageSizes, 0));

        } else if (includeNotSpecified) {
            // เอาแค่ที่ไม่ระบุ storage (= 0 หรือ null)
            spec = spec.and(withStorageGbEqualsOrNull(0));

        } else if (filterStorageSizes != null && !filterStorageSizes.isEmpty()) {
            // เอาแค่ storage ที่ระบุ
            spec = spec.and(withStorageGbIn(filterStorageSizes));
        }

        // เพิ่ม sorting
        spec = spec.and(withSorting(sortField, sortDirection));

        return spec;
    }
}