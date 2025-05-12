package com.example.backend.dtos;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateSaleItemDto {
    private Integer brandId;
    private String model;
    private String description;
    private Integer price;
    private Integer ramGb;
    private Integer storageGb;
    private BigDecimal screenSizeInch;
    private String color;
    private Integer quantity;
}

