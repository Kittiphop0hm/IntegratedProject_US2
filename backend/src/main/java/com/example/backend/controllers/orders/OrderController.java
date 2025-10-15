package com.example.backend.controllers.orders;

import com.example.backend.dtos.orders.GetBuyerOrderDto;
import com.example.backend.dtos.orders.PlaceOrderRequestDto;
import com.example.backend.dtos.orders.PlaceOrderResponseDto;
import com.example.backend.entities.AuthUserDetail;
import com.example.backend.entities.Order;
import com.example.backend.services.orders.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2/orders")
@CrossOrigin(origins = "${app.cors.allowed-origins}", allowCredentials = "true")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("")
    public ResponseEntity<List<PlaceOrderResponseDto>> createOrder(@RequestBody List<PlaceOrderRequestDto> orders) {
        return ResponseEntity.status(201).body(orderService.createOrder(orders));
    }
    @GetMapping("/{id}")
    public ResponseEntity<GetBuyerOrderDto> getOrderById(
            @PathVariable Integer id,
            @AuthenticationPrincipal AuthUserDetail principal) {
        return ResponseEntity.ok(orderService.getBuyerOrderById(id, principal));
    }
}
