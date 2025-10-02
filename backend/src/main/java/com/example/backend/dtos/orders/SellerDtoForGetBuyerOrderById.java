package com.example.backend.dtos.orders;

import lombok.Data;

@Data
public class SellerDtoForGetBuyerOrderById {
    private Integer id;
    private String email;
    private String fullName;
    private String userType;
    private String nickName;
}
