package com.example.backend.dtos.brands;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUpdateBrandDto {
    private String name;
    private String websiteUrl;
    private String countryOfOrigin;
    private Boolean isActive;

    public void setName(String name) {
        if (name != null) {
            name = name.trim();
            this.name = name.isEmpty() ? null : name;
        } else {
            this.name = null;
        }
    }

    public void setIsActive(Boolean isActive) {
        if (isActive == null) {
            isActive = true;
        }
        this.isActive = isActive;
    }
}
