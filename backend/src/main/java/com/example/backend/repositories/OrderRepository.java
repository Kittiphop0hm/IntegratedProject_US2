package com.example.backend.repositories;

import com.example.backend.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findOrdersBySeller_Id(Integer sellerId, Pageable pageable);
    Page<Order> findOrdersByBuyer_Id(Integer buyerId, Pageable pageable);
}
