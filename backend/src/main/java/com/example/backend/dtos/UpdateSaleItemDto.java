package com.example.backend.dtos;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
public class UpdateSaleItemDto {

    private Integer brandId;

    @NotBlank(message = "Model is required")
    private String model;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be 0 or more")
    private Integer price;

//    @PositiveOrZero(message = "RAM must be 0 or more")
    private Integer ramGb;

//    @PositiveOrZero(message = "Storage must be 0 or more")
    private Integer storageGb;

//    @DecimalMin(value = "0.0", inclusive = false, message = "Screen size must be greater than 0")
    private BigDecimal screenSizeInch;

//    @NotBlank(message = "Color is required")
    private String color;

    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Quantity must be 0 or more")
    private Integer quantity;
}
