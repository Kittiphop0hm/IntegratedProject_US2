package com.example.backend.specifications;

import com.example.backend.entities.SaleItem;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SaleItemSearchSpecification {

    public static Specification<SaleItem> withSearchKeyword(String searchKeyword) {
        return (root, query, cb) -> {
            if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
                return cb.conjunction();
            }

            String likePattern = "%" + searchKeyword.toLowerCase().trim() + "%";

            // แปลง CLOB/TEXT เป็น String
            Expression<String> descriptionAsString = root.get("description").as(String.class);

            return cb.or(
                    cb.like(cb.lower(descriptionAsString), likePattern),
                    cb.like(cb.lower(root.get("model")), likePattern),
                    cb.like(cb.lower(root.get("color")), likePattern)
            );
        };
    }
}