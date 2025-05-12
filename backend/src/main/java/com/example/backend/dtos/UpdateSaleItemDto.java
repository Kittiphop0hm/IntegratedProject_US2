package com.example.backend.dtos;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
public class UpdateSaleItemDto {
    private BrandDto brand;
    private String model;
    private String description;
    private Integer price;
    private Integer ramGb;
    private Integer storageGb;
    private BigDecimal screenSizeInch;
    private String color;
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
        this.description = description == null ? null : description.trim();
    }

    public void setColor(String color) {
        this.color = (color == null || color.trim().isEmpty()) ? null : color.trim();
    }
}
