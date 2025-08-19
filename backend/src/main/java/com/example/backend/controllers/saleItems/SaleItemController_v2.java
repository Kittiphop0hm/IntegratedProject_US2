package com.example.backend.controllers.saleItems;

import com.example.backend.dtos.saleItems.*;


import com.example.backend.services.saleitems.SaleItemService_v1;
import com.example.backend.services.saleitems.SaleItemService_v2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v2/sale-items")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class SaleItemController_v2 {
    @Autowired
    private SaleItemService_v1 saleItemServiceV1;
    @Autowired
    private SaleItemService_v2 saleItemServiceV2;


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

        return ResponseEntity.ok(saleItemServiceV1.mergeFilterAndSortSaleItem(
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


    @GetMapping("{id}")
    public ResponseEntity<ResponseSaleItemsDto> getProductById(@PathVariable Integer id) {
        return ResponseEntity.ok(saleItemServiceV2.findByid(id));
    }

    @PostMapping("")
    public ResponseEntity<?> createProduct(
            @ModelAttribute SaleItemDetailForCreateOrUpdateDto newSaleItem ,
            @RequestParam List<MultipartFile> images
    ){
        try{
            ResponseSaleItemsDto result = saleItemServiceV2.createProduct(newSaleItem , images);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Sale item create failed: " + e.getMessage());
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        saleItemServiceV2.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Integer id ,
            @ModelAttribute SaleItemWithImageInfo data
            ){
        ResponseSaleItemsDto res = saleItemServiceV2.updateProduct(id, data);
        return ResponseEntity.ok(res);
    }
}

