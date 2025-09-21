package com.example.backend.dtos.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileResponseDto {
    private Integer id;
    private String email;
    private String fullName;
    private String userType;
    private String nickName;
    private String phoneNumber;
    private String bankName;
    private String bankAccount;
    private String accessToken;
}
