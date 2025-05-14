package com.example.backend.dtos.saleItems;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListSaleItemsDto {
    private Integer id;
    private String model;
    private String brandName;
    private Integer price;
    private Integer storageGb;
    private Integer ramGb;
    private String color;
}
