//package com.example.backend.controllers.users;
//
//import com.example.backend.dtos.saleItems.GetSaleItemDto;
//import com.example.backend.dtos.saleItems.PageDto;
//import com.example.backend.entities.AuthUserDetail;
//import com.example.backend.services.saleitems.SaleItemService_v1;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//



//controller pbi25 ไม่ชัวร์ว่าถูกไหม
//@RestController
//@RequestMapping("/v2/sellers")
//@CrossOrigin(origins = "${app.cors.allowed-origins}", allowCredentials = "true")
//public class SellerController {
//
//    @Autowired
//    private SaleItemService_v1 saleItemService_v1;
//
//    // PBI 25: GET /v2/sellers/{id}/sale-items
//    @GetMapping("/{id}/sale-items")
//    @PreAuthorize("hasAuthority('SELLER') and #id == authentication.principal.id")
//    public ResponseEntity<PageDto<GetSaleItemDto>> getSaleItemsBySeller(
//            @PathVariable("id") Integer id,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "id") String sortField,
//            @RequestParam(defaultValue = "asc") String sortDirection,
//            Authentication authentication) {
//
//        AuthUserDetail userDetails = (AuthUserDetail) authentication.getPrincipal();
//        Integer userIdFromToken = userDetails.getId();
//
//        if (!id.equals(userIdFromToken)) {
//            return ResponseEntity.status(403).build();
//        }
//
//        PageDto<GetSaleItemDto> response = saleItemService_v1.getSaleItemsBySellerId(
//                userIdFromToken, page, size, sortField, sortDirection);
//
//        return ResponseEntity.ok(response);
//    }
//}