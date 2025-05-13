package com.example.backend.dtos;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
public class AddUpdateSaleItemDto {
    private BrandDto brand;
    private String model;
    private String description;
    private Integer price;
    private Integer ramGb;
    private Integer storageGb;
    private BigDecimal screenSizeInch;
    private String color;

    @Min(value = 0)
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
        if (description != null) {
            description = description.trim();
            this.description = description.isEmpty() ? null : description;
        } else {
            this.description = null;
        }
    }

    public void setColor(String color) {
        this.color = (color == null || color.trim().isEmpty()) ? null : color.trim();
    }

    public void setQuantity(Integer quantity) {
        if (quantity == null || quantity < 0) {
            this.quantity = 1;
            return;
        }
        this.quantity = quantity;
    }
}
