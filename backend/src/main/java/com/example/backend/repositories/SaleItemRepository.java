package com.example.backend.repositories;

import com.example.backend.entities.SaleItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Integer> {

    // เพิ่มเมธอดนี้สำหรับ PBI 25
    Page<SaleItem> findBySellerId(Integer sellerId, Pageable pageable);

    List<SaleItem> findAllByOrderByCreatedOn();
}