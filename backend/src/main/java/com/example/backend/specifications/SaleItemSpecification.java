package com.example.backend.specifications;

import com.example.backend.entities.SaleItem;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SaleItemSpecification {

    public static Specification<SaleItem> withSearchKeyword(String searchKeyword) {
        return (root, query, cb) -> {

            if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
                return cb.conjunction();
            }

            String[] words = searchKeyword.toLowerCase().split("\\s+");

            Expression<String> description = cb.lower(root.get("description").as(String.class));
            Expression<String> model = cb.lower(root.get("model"));
            Expression<String> color = cb.lower(root.get("color"));

            List<Predicate> allWordConditions = new ArrayList<>();

            for (String word : words) {
                word = word.trim();
                if (word.isEmpty()) continue;

                String pattern = "%" + word + "%";

                Predicate wordFound = cb.or(
                        cb.like(description, pattern),
                        cb.like(model, pattern),
                        cb.like(color, pattern)
                );

                allWordConditions.add(wordFound);
            }

            if (allWordConditions.isEmpty()) {
                return cb.conjunction();
            }

            return cb.and(allWordConditions.toArray(new Predicate[0]));
        };
    }
    public static Specification<SaleItem> withBrands(List<String> brands) {
        return (root, query, cb) -> {
            if (brands == null || brands.isEmpty()) {
                return cb.conjunction();
            }

            Join<Object, Object> brandJoin = root.join("brand", JoinType.INNER);
            return brandJoin.get("name").in(brands);
        };
    }

    public static Specification<SaleItem> withPriceRange(Integer minPrice, Integer maxPrice) {
        return (root, query, cb) -> {
            List<Predicate> conditions = new ArrayList<>();

            if (minPrice != null) {
                conditions.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            if (maxPrice != null) {
                conditions.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            if (conditions.isEmpty()) {
                return cb.conjunction();
            }

            return cb.and(conditions.toArray(new Predicate[0]));
        };
    }


    public static Specification<SaleItem> withStorageGbIn(List<Integer> storageSizes) {
        return (root, query, cb) -> {
            if (storageSizes == null || storageSizes.isEmpty()) {
                return cb.conjunction();
            }
            return root.get("storageGb").in(storageSizes);
        };
    }


    public static Specification<SaleItem> withStorageGbEqualsOrNull(Integer storageValue) {
        return (root, query, cb) -> {
            if (storageValue == null) {
                return cb.isNull(root.get("storageGb"));
            }

            return cb.or(
                    cb.equal(root.get("storageGb"), storageValue),
                    cb.isNull(root.get("storageGb"))
            );
        };
    }


    public static Specification<SaleItem> withStorageGbInOrEqualsOrNull(List<Integer> storageSizes, Integer notSpecifiedValue) {
        return (root, query, cb) -> {
            List<Predicate> conditions = new ArrayList<>();

            if (storageSizes != null && !storageSizes.isEmpty()) {
                conditions.add(root.get("storageGb").in(storageSizes));
            }

            if (notSpecifiedValue != null) {
                conditions.add(cb.or(
                        cb.equal(root.get("storageGb"), notSpecifiedValue),
                        cb.isNull(root.get("storageGb"))
                ));
            }

            if (conditions.isEmpty()) {
                return cb.conjunction();
            }

            if (conditions.size() == 1) {
                return conditions.get(0);
            }

            return cb.or(conditions.toArray(new Predicate[0]));
        };
    }


    public static Specification<SaleItem> withSorting(String sortField, String sortDirection) {
        return (root, query, cb) -> {

            if (query.getResultType() == Long.class || query.getResultType() == long.class) {
                return cb.conjunction();
            }


            boolean isAscending = true;
            if (sortDirection != null && "desc".equals(sortDirection.trim().toLowerCase())) {
                isAscending = false;
            }


            String field = "";
            if (sortField != null) {
                field = sortField.trim().toLowerCase();
            }

            Path<?> orderPath;

            if (field.equals("brand") || field.equals("brand.name") || field.equals("brand_name")) {

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
                orderPath = root.get("createdOn");

            } else {
                orderPath = root.get("createdOn");
                isAscending = true;
            }

            Order order = isAscending ? cb.asc(orderPath) : cb.desc(orderPath);
            query.orderBy(order);

            return cb.conjunction();
        };
    }

    public static Specification<SaleItem> buildFilterSpecification(
            String searchKeyword,
            List<String> filterBrands,
            Integer minPrice,
            Integer maxPrice,
            List<Integer> filterStorageSizes,
            boolean includeNotSpecified,
            String sortField,
            String sortDirection) {


        Specification<SaleItem> spec = Specification.where(null);


        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            spec = spec.and(withSearchKeyword(searchKeyword));
        }

        if (filterBrands != null && !filterBrands.isEmpty()) {
            spec = spec.and(withBrands(filterBrands));
        }

        if (minPrice != null || maxPrice != null) {
            spec = spec.and(withPriceRange(minPrice, maxPrice));
        }

        if (includeNotSpecified && (filterStorageSizes != null && !filterStorageSizes.isEmpty())) {
            spec = spec.and(withStorageGbInOrEqualsOrNull(filterStorageSizes, 0));

        } else if (includeNotSpecified) {
            spec = spec.and(withStorageGbEqualsOrNull(0));

        } else if (filterStorageSizes != null && !filterStorageSizes.isEmpty()) {
            spec = spec.and(withStorageGbIn(filterStorageSizes));
        }

        spec = spec.and(withSorting(sortField, sortDirection));

        return spec;
    }
}