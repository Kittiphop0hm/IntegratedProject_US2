package com.example.backend.dtos.orders;

import lombok.Data;

import java.time.Instant;
import java.util.List;
@Data
public class GetAllBuyerOrderDto {
    private Integer id;
    private Integer buyerId;
    private SellerForGetAllBuyerOrderDto seller;
    private Instant orderDate;
    private Instant paymentDate;
    private String shippingAddress;
    private String orderNote;
    private List<OrderItemDto> orderItems;
    private String orderStatus;
}
