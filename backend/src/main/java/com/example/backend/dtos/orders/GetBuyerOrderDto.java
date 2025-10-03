package com.example.backend.dtos.orders;

import lombok.Data;
import java.time.Instant;
import java.util.List;

@Data
public class GetBuyerOrderDto {
    private Integer id;
    private Integer buyerId;
    private SellerDtoForGetBuyerOrderById seller;
    private Instant orderDate;
    private String shippingAddress;
    private String orderNote;
    private List<OrderItemDto> orderItems;
    private String orderStatus;
}
