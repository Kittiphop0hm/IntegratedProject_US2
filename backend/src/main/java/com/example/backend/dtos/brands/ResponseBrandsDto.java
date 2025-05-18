package com.example.backend.dtos.brands;

import com.example.backend.entities.SaleItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
public class ResponseBrandsDto {
    private Integer id;
    private String name;
    private String websiteUrl;
    private String countryOfOrigin;
    private Boolean isActive;
    private Integer noOfSaleItems;
    @JsonIgnore
    private Set<SaleItem> saleItems;

    public Integer getNoOfSaleItems() {
        return saleItems.size();
    }
}
