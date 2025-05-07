package com.example.backend.controllers;

import com.example.backend.dtos.*;
import com.example.backend.entities.Brand;
import com.example.backend.entities.SaleItem;
import com.example.backend.services.BrandService;
import com.example.backend.services.SaleItemService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;

@RestController
@RequestMapping("/v1/sale-items")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class SaleItemController {
    @Autowired
    private SaleItemService service;
    @Autowired
    private ModelMapper modelMapper;


    @GetMapping("")
    public ResponseEntity<List<ListItemsDto>> getAllSaleItems() {
        List<SaleItem> saleItems = service.findAll();
        service.checkValues(saleItems);
        List<ListItemsDto> listItemsDto = saleItems.stream().map(saleItem -> modelMapper.map(saleItem, ListItemsDto.class)).toList();
        return ResponseEntity.ok(listItemsDto);
    }

    @GetMapping("{id}")
    public ResponseEntity<GetItemDto> getSaleItemById(@PathVariable Integer id) {
        SaleItem saleItem = service.findById(id);
        service.checkValue(saleItem);
        return ResponseEntity.ok(modelMapper.map(saleItem, GetItemDto.class));
    }

    @PostMapping("")
    public ResponseEntity<SaleItemResponseDto> createSaleItem(
            @Valid @RequestBody CreateSaleItemDto createSaleItemDto) {
        SaleItemResponseDto responseDto = service.createSaleItem(createSaleItemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<SaleItemResponseDto> updateSaleItem(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateSaleItemDto updateSaleItemDto) {
        SaleItemResponseDto responseDto = service.updateSaleItem(id, updateSaleItemDto);
        return ResponseEntity.ok(responseDto);
    }
   @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSaleItemById(@PathVariable Integer id) {
        service.deleteSaleItem(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
   }

}
