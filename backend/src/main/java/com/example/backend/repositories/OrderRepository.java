package com.example.backend.repositories;

import com.example.backend.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findOrdersBySeller_IdOrderByIdDesc(Integer sellerId, Pageable pageable);
    Page<Order> findOrdersBySeller_IdAndOrderStatusOrderByIdDesc(Integer sellerId, String orderStatus, Pageable pageable);
    Page<Order> findOrdersByBuyer_IdAndOrderStatusOrderByIdDesc(Integer buyerId, String orderStatus, Pageable pageable);
    List<Order> findOrdersByOrderStatusAndIsNewOrderAndSeller_Id(String orderStatus, Boolean isNewOrder, Integer sellerId);
}
