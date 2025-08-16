package com.example.backend.dtos.saleItems;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListSaleItemsDto {
    private Integer id;
    private String brandName;
    private String model;
    private Integer ramGb;
    private Integer storageGb;
    private String color;
    private Integer price;
}
