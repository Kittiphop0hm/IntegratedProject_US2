package com.example.backend.repositories;

import com.example.backend.entities.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface SaleItemPageRepository extends JpaRepository<SaleItem, Integer>, JpaSpecificationExecutor<SaleItem> {


}