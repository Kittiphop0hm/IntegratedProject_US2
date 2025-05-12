package com.example.backend.dtos;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateSaleItemDto {

//    @NotNull(message = "Brand ID is required")
    private BrandDto brand;

//    @NotBlank(message = "Model is required")
//    @Size(max = 60, message = "Model name must not exceed 60 characters")
    private String model;

//    @NotBlank(message = "Description is required")
    private String description;

//    @NotNull(message = "Price is required")
//    @Min(value = 1, message = "Price must be greater than 0")
    private Integer price;

//    @Min(value = 6, message = "RAM must be greater than 6")
    private Integer ramGb;
//    @Min(value = 1, message = "Storage must be greater than 0")
    private Integer storageGb;
//    @DecimalMin(value = "0.1", message = "Screen size must be greater than 0")
    private BigDecimal screenSizeInch;
//    @NotBlank(message = "Color is required")
    private String color;
//    @NotNull(message = "Quantity is required")
//    @PositiveOrZero(message = "Quantity must be 0 or more")
    private Integer quantity;


    public void setModel(String model) {
        if (model != null) {
            model = model.trim();
            this.model = model.isEmpty() ? null : model;
        } else {
            this.model = null;
        }
    }
    public void setDescription(String description) {
        this.description = (description == null || description.trim().isEmpty()) ? null : description.trim();
    }

    public void setColor(String color) {
        this.color = (color == null || color.trim().isEmpty()) ? null : color.trim();
    }


}

