package com.example.backend.controllers;

import com.example.backend.dtos.saleItems.AddUpdateSaleItemDto;
import com.example.backend.dtos.saleItems.GetSaleItemDto;
import com.example.backend.dtos.saleItems.ListSaleItemsDto;
import com.example.backend.dtos.saleItems.ResponseSaleItemsDto;
import com.example.backend.services.BrandService;
import com.example.backend.services.SaleItemService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/sale-items")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class SaleItemController {
    @Autowired
    private SaleItemService service;
    @Autowired
    private ModelMapper modelMapper;
   @Autowired
   private BrandService brandService;

    @GetMapping("")
    public ResponseEntity<List<ListSaleItemsDto>> getAllSaleItems() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<GetSaleItemDto> getSaleItemById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping("")
    public ResponseEntity<ResponseSaleItemsDto> createSaleItem(
            @RequestBody AddUpdateSaleItemDto createSaleItemDto) {
        ResponseSaleItemsDto responseDto = service.createSaleItem(createSaleItemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseSaleItemsDto> updateSaleItem(@PathVariable Integer id, @RequestBody AddUpdateSaleItemDto req) {
        ResponseSaleItemsDto saleItemResponseDto = service.updateSaleItem(id, req);
        return ResponseEntity.ok(saleItemResponseDto);
    }

   @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSaleItemById(@PathVariable Integer id) {
        service.deleteSaleItem(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
   }
}
