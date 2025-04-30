package com.example.backend.controllers;

import com.example.backend.dtos.GetItemDto;
import com.example.backend.dtos.ListItemsDto;
import com.example.backend.entities.SaleItem;
import com.example.backend.services.SaleItemService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/sale-items")
public class SaleItemController {
    @Autowired
    private SaleItemService service;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public ResponseEntity<List<ListItemsDto>> getAllSaleItems() {
        List<SaleItem> saleItems = service.findAll();
        List<ListItemsDto> listItemsDto = saleItems.stream().map(saleItem -> modelMapper.map(saleItem, ListItemsDto.class)).toList();
        return ResponseEntity.ok(listItemsDto);
    }

    @GetMapping("{id}")
    public ResponseEntity<GetItemDto> getSaleItemById(@PathVariable Integer id) {
        SaleItem saleItem = service.findById(id);
        return ResponseEntity.ok(modelMapper.map(saleItem, GetItemDto.class));
    }
}
