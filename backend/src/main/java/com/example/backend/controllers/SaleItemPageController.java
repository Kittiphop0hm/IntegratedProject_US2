package com.example.backend.controllers;

import com.example.backend.dtos.saleItems.GetSaleItemDto;
import com.example.backend.dtos.saleItems.ListSaleItemsDto;
import com.example.backend.services.SaleItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2/sale-items")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class SaleItemPageController {
    @Autowired
    private SaleItemService service;

    @GetMapping("filter")
    public ResponseEntity<List<ListSaleItemsDto>> filterSaleItemsByBrandName(@RequestParam(defaultValue = "") List<String> filterBrands) {
        return ResponseEntity.ok(service.filterSaleItemsByBrandName(filterBrands));
    }
}
