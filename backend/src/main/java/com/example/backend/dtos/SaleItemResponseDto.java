package com.example.backend.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
public class SaleItemResponseDto {
    private Integer id;
    private String brandName;
    private Integer brandId;
    private String model;
    private String description;
    private Integer price;
    private Integer ramGb;
    private Integer storageGb;
    private BigDecimal screenSizeInch;
    private String color;
    private Integer quantity;
    private Instant createdOn;
    private Instant updatedOn;
}
