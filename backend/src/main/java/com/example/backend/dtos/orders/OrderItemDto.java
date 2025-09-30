package com.example.backend.dtos.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"no", "saleItemId", "price", "quantity", "description"})
public class OrderItemDto {
    @JsonProperty("no")
    private Integer id;
    private Integer saleItemId;
    private Integer price;
    private Integer quantity;
    private String description;
}
