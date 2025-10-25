package com.example.backend.dtos.orders;

import com.example.backend.entities.OrderItem;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class PlaceOrderRequestDto {
    private Integer buyerId;
    private Integer sellerId;
    private Instant orderDate;
    private String shippingAddress;
    private String orderNote;
    private List<OrderItemDto> orderItems;
    private String orderStatus;
    private Boolean isNewOrder;
}
