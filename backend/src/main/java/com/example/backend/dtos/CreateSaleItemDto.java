package com.example.backend.dtos;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateSaleItemDto {
    @NotNull(message = "Brand ID is required")
    private Integer brandId;
    @NotBlank(message = "Model is required")
    @Size(max = 100, message = "Model name must not exceed 100 characters")
    private String model;
    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    @NotNull(message = "Price is required")
    @Min(value = 1, message = "Price must be greater than 0")
    private Integer price;
//    @Min(value = 1, message = "RAM must be greater than 0")
    private Integer ramGb;
//    @Min(value = 1, message = "Storage must be greater than 0")
    private Integer storageGb;
//    @DecimalMin(value = "0.1", message = "Screen size must be greater than 0")
    private BigDecimal screenSizeInch;
//    @NotBlank(message = "Color is required")
    private String color;
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}

