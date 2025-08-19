package com.example.backend.controllers.saleItems;

import com.example.backend.dtos.saleItems.SaleItemDetailForCreateOrUpdateDto;
import com.example.backend.dtos.saleItems.GetSaleItemDto;
import com.example.backend.dtos.saleItems.ListSaleItemsDto;
import com.example.backend.dtos.saleItems.ResponseSaleItemsDto;
import com.example.backend.services.saleitems.SaleItemService_v1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/sale-items")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class SaleItemController_v1 {
    @Autowired
    private SaleItemService_v1 service;

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
            @RequestBody SaleItemDetailForCreateOrUpdateDto createSaleItemDto) {
        ResponseSaleItemsDto responseDto = service.createSaleItem(createSaleItemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseSaleItemsDto> updateSaleItem(@PathVariable Integer id, @RequestBody SaleItemDetailForCreateOrUpdateDto req) {
        ResponseSaleItemsDto saleItemResponseDto = service.updateSaleItem(id, req);
        return ResponseEntity.ok(saleItemResponseDto);
    }

   @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSaleItemById(@PathVariable Integer id) {
        service.deleteSaleItem(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
   }
}
