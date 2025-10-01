package com.example.backend.dtos.orders;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class PlaceOrderResponseDto {
    private Integer id;
    private Integer buyerId;
    private SellerForPlaceOrderDto seller;
    private Instant orderDate;
    private Instant paymentDate;
    private String shippingAddress;
    private String orderNote;
    private List<OrderItemDto> orderItems;
    private String orderStatus;

}
