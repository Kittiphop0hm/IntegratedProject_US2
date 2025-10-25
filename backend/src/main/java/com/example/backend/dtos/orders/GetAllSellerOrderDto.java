package com.example.backend.dtos.orders;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class GetAllSellerOrderDto {
    private Integer id;
    private BuyerForGetAllSellerOrderDto buyer;
    private Integer sellerId;
    private Instant orderDate;
    private Instant paymentDate;
    private String shippingAddress;
    private String orderNote;
    private List<OrderItemDto> orderItems;
    private String orderStatus;
    private Boolean isNewOrder;
}
