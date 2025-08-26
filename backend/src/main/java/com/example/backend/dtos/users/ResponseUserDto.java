package com.example.backend.dtos.users;

import lombok.Data;

@Data
public class ResponseUserDto {
    private Integer id;
    private String nickName;
    private String email;
    private String phoneNumber;
    private String isActive;
    private String userType;
}
