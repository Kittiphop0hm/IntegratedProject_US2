package com.example.backend.dtos.saleItems;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class GetSaleItemDto {
    private Integer id;
    private String brandName;
    private String color;
    private String description;
    private Integer quantity;
    private String model;
    private Integer price;
    private Integer ramGb;
    private Integer storageGb;
    private BigDecimal screenSizeInch;
    private Instant createdOn;
    private Instant updatedOn;
}
