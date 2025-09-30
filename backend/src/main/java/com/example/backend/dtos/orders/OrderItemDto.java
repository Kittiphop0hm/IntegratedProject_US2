package com.example.backend.dtos.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemDto {
    private Integer no;
    private Integer saleItemId;
    private Integer price;
    private Integer quantity;
    private String description;
}
