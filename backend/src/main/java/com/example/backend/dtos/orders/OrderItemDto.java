package com.example.backend.dtos.orders;

import lombok.Data;

@Data
public class OrderItemDto {
    private Integer saleItemId;
    private Integer price;
    private Integer quantity;
    private String description;
}
