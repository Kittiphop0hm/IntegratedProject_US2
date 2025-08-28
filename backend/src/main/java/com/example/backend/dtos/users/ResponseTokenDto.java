package com.example.backend.dtos.users;

import lombok.Data;

@Data
public class ResponseTokenDto {
    private String access_token;
    private String refresh_token;
}
