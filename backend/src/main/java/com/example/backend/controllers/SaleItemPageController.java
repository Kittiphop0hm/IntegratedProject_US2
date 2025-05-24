package com.example.backend.controllers;

import com.example.backend.dtos.saleItems.GetSaleItemDto;
import com.example.backend.dtos.saleItems.ListSaleItemsDto;
import com.example.backend.dtos.saleItems.PageDto;
import com.example.backend.entities.SaleItem;
import com.example.backend.services.SaleItemService;
import com.example.backend.utils.ListMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2/sale-items")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class SaleItemPageController {
    @Autowired
    private SaleItemService service;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public ResponseEntity<PageDto<GetSaleItemDto>> filterSaleItemsByBrandName(
            @RequestParam(defaultValue = "") List<String> filterBrands,
            @RequestParam(defaultValue = "") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam Integer page ,
            @RequestParam(defaultValue = "10")  Integer size
    ) {
        return ResponseEntity.ok(service.mergeFilterAndSortSaleItem(filterBrands, sortField, sortDirection , page , size));
    }

//    @GetMapping("")
//    public ResponseEntity<List<GetSaleItemDto>> filterSaleItemsByBrandName(
//            @RequestParam(defaultValue = "") List<String> filterBrands,
//            @RequestParam(defaultValue = "") String sortField,
//            @RequestParam(defaultValue = "asc") String sortDirection
//    ) {
//        return ResponseEntity.ok(service.mergeFilterAndSortSaleItem(filterBrands, sortField, sortDirection));
//    }
}

