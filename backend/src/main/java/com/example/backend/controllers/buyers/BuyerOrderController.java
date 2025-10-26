package com.example.backend.controllers.buyers;

import com.example.backend.dtos.orders.GetAllBuyerOrderDto;
import com.example.backend.dtos.saleItems.PageDto;
import com.example.backend.entities.AuthUserDetail;
import com.example.backend.services.orders.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2/users/{id}/orders")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class BuyerOrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("")
    public ResponseEntity<PageDto<GetAllBuyerOrderDto>> getOrderBuyerPage(
            @PathVariable Integer id,
            @RequestParam Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "COMPLETED") String orderStatus,
            @AuthenticationPrincipal AuthUserDetail principal
    ) {
        return ResponseEntity.ok(orderService.getAllBuyerOrdersById(id, orderStatus, page, size, sortField, principal));
    }
}