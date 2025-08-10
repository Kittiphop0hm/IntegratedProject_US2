package com.example.backend.controllers;

import com.example.backend.dtos.saleItems.GetSaleItemDto;
import com.example.backend.dtos.saleItems.PageDto;
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

    @GetMapping("")
    public ResponseEntity<PageDto<GetSaleItemDto>> filterSaleItemsByBrandName(
            @RequestParam(defaultValue = "") List<String> filterBrands,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(defaultValue = "") List<String> filterStorageSizes,
            @RequestParam(defaultValue = "") String sortField,
            @RequestParam(defaultValue = "") String sortDirection,
            @RequestParam Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {

        return ResponseEntity.ok(service.mergeFilterAndSortSaleItem(
                filterBrands,
                minPrice,
                maxPrice,
                filterStorageSizes,
                sortField,
                sortDirection,
                page,
                size
        ));
    }
}