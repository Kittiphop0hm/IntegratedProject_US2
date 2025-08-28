package com.example.backend.dtos.users;

import lombok.Data;

@Data
public class RequestLoginDto {
    private String email;
    private String password;
}
