package com.example.backend.controllers.sellers;

import com.example.backend.dtos.saleItems.GetSaleItemDto;
import com.example.backend.dtos.saleItems.PageDto;
import com.example.backend.dtos.saleItems.ResponseSaleItemsDto;
import com.example.backend.dtos.saleItems.SaleItemDetailForCreateOrUpdateDto;
import com.example.backend.dtos.saleItems.sellers.GetSaleItemSellerDto;
import com.example.backend.entities.AuthUserDetail;
import com.example.backend.services.saleitems.SaleItemService_v1;
import com.example.backend.services.saleitems.SaleItemService_v2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v2/sellers/{id}/sale-items")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class SaleItemController {

    @Autowired
    private SaleItemService_v1 saleItemServiceV1;
    @Autowired
    private SaleItemService_v2 saleItemServiceV2;


    @GetMapping("")
    public ResponseEntity<PageDto<GetSaleItemSellerDto>> getSaleItemList (
            @PathVariable Integer id,
            @RequestParam Integer page,
            @RequestParam(defaultValue = "10") Integer size ,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection ,
            @AuthenticationPrincipal AuthUserDetail principal

    ) {

        return ResponseEntity.ok(saleItemServiceV1.getSaleItemListBySeller(id, page, size, sortField, sortDirection , principal));
    }


    @PostMapping("")
    public ResponseEntity<?> createProduct(
            @PathVariable Integer id,
            @ModelAttribute SaleItemDetailForCreateOrUpdateDto newSaleItem ,
            @RequestParam(required = false) List<MultipartFile> images
    ){
        try{
            ResponseSaleItemsDto result = saleItemServiceV2.createProduct(id,newSaleItem , images);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Sale item create failed: " + e.getMessage());
        }
    }
}
