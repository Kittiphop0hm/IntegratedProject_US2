package com.example.backend.controllers.sellers;

import com.example.backend.dtos.orders.GetAllSellerOrderDto;
import com.example.backend.dtos.orders.PlaceOrderResponseDto;
import com.example.backend.dtos.saleItems.PageDto;
import com.example.backend.entities.AuthUserDetail;
import com.example.backend.services.orders.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2/sellers/{sid}/orders")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class SellerOrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("")
    public ResponseEntity<PageDto<GetAllSellerOrderDto>> getOrderSellerPage(
            @PathVariable Integer sid,
            @RequestParam(defaultValue = "COMPLETED") String orderStatus,
            @RequestParam Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortField,
            @AuthenticationPrincipal AuthUserDetail principal
    ) {
    return ResponseEntity.ok(orderService.getOrderBySellerId(sid, orderStatus, page, size, sortField, principal));
    }

    @GetMapping("/{oid}")
    public ResponseEntity<GetAllSellerOrderDto> getSellerOrderDetail(
            @PathVariable Integer sid,
            @PathVariable Integer oid,
            @AuthenticationPrincipal AuthUserDetail principal
    ) {
        return ResponseEntity.ok(orderService.getOrderSellerByOrderId(oid, principal));
    }


}
