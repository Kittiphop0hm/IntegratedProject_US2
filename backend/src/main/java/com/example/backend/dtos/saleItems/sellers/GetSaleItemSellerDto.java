package com.example.backend.dtos.saleItems.sellers;

import com.example.backend.dtos.users.sellers.GetUserSellerDto;
import lombok.Data;



@Data
public class GetSaleItemSellerDto {
        private Integer id;
        private String model;
        private String brandName;
        private Integer price;
        private Integer storageGb;
        private Integer ramGb;
        private String color;
        private GetUserSellerDto seller;
}
