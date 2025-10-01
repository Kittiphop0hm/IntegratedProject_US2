package com.example.backend.dtos.saleItems.sellers;

import com.example.backend.dtos.files.ListFilesDto;
import com.example.backend.dtos.users.sellers.GetUserSellerDto;
import com.example.backend.entities.User;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class ResponseSaleItemsWithSellerDto {
    private Integer id;
    private String model;
    private String brandName;
    private String description;
    private Integer price;
    private Integer ramGb;
    private BigDecimal screenSizeInch;
    private Integer quantity;
    private Integer storageGb;
    private String color;
    private List<ListFilesDto> saleItemImages;
    private GetUserSellerDto seller;
    private Instant createdOn;
    private Instant updatedOn;

};
